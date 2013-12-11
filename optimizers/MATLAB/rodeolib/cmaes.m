function [stat] = cmaes(problem_specification, optimizer_parameters, run_parameters)
% [stat] = cmaes(problem_specification, optimizer_parameters, run_parameters)
%
% Implementation of the CMA-ES algorithm
%
% Input:
% - problem_specification            - A struct containing the problem definition with:
%   - problem_type                   - A string holding description of the problem type
%   - problem_name                   - A string holding the name of the test function
%   - objective_function             - A function handle to the objective function
%   - N                              - The number of dimensions of the search space
%   - lb                             - A vector of the upper bounds of the search interval
%   - ub                             - A vector of the lower bounds of the search interval
% - run_parameters                   - A struct containing the run parameters with:
%   - max_generations_termination    - The maximum number of generations
%   - max_evaluations_termination    - The maximum number of evaluations
%   - min_fitness_termination        - Stop when a solution is found below this value
%   - history_statistics             - Maintain history statistics
%   - internal_parameters_statistics - Maintain statistics of the internal parameters
%   - report_intermediate            - Report statistics while running
%   - report_after_termination       - Report after termination
%   - report_fct                     - A handle to the report function
%   - restart_log_intermediate       - Maintain a logfile to allow for restarts
%   - restart_log_after_termination  - Store a logfile to allow for restarts after completing an optimization run
%   - restart_log_logfile            - Filename of the restart logfile
% - optimizer_parameters             - A struct containing algorithm specific parameters
%   - bch_fct                        - A handle to the box constraint handling function
%
% Output:
% - stat                   - 
%
% Last modified: December 20, 2010

	% Problem specification
	fitnessfct = [problem_specification.path, '/', problem_specification.routine];
	N          = problem_specification.dimension;
	lb         = problem_specification.lowerbound;
	ub         = problem_specification.upperbound;

	% Run parameters
        stopeval                      = readField(run_parameters, 'termination_max_evaluations', -1);
        stopgen                       = readField(run_parameters, 'termination_max_generations', -1);
	minfitness                    = readField(run_parameters, 'termination_min_fitness', -inf);
	history_statistics            = readField(run_parameters, 'history_statistics', true);
	internal_parameter_statistics = readField(run_parameters, 'internal_parameter_statistics', true);
	report_intermediate           = readField(run_parameters, 'report_intermediate', false);
	report_after_termination      = readField(run_parameters, 'report_after_termination', false);
	report_fct                    = readField(run_parameters, 'report_fct', 'plot_runstats_cmaes');
	do_restart                    = readField(run_parameters, 'do_restart', false);
	restart_log_intermediate      = readField(run_parameters, 'restart_log_intermediate', false);
	restart_log_after_termination = readField(run_parameters, 'restart_log_after_termination', false);
	restart_log_logfile           = readField(run_parameters, 'restart_log_logfile', 'log_test_cmaes.mat');

	% Optimizer parameters
	lambda  = readField(optimizer_parameters, 'lambda', 4 + floor(3 * log(N)));  
	mu      = readField(optimizer_parameters, 'mu', floor(lambda / 2));
	bch_fct = readField(optimizer_parameters, 'constraint_routine', ''); 

	% Set the weights for recombination
	weights = log(mu + 1) - log(1:mu);
	weights = weights / sum(weights);
	mueff = sum(weights)^2 / sum(weights.^2);

	% Set parameters
	cc = 4 / (N + 4);
	cs = (mueff + 2) / (N + mueff + 3);
	mucov = mueff;
	ccov = (1 / mucov) * 2 / (N + 1.4)^2 + (1 - 1 / mucov) * ((2 * mueff - 1) / ((N + 2)^2 + 2 * mueff));
	damps = 1 + 2 * max(0, sqrt((mueff - 1) / (N + 1)) - 1) + cs;
	chiN = N^0.5 * (1 - 1 / (4 * N) + 1 / (21 * N^2));

	% Initialize xmean and step size
	if (isfield(optimizer_parameters, 'xmean_init'))
		xmean = optimizer_parameters.xmean_init;
	else
		%xmean = (lb + ub) / 2;
		xmean = lb + (ub - lb) .* rand(N,1);
	end
	if (isfield(optimizer_parameters, 'sigma_init'))
		sigma = optimizer_parameters.sigma_init;
	else
		sigma = (norm(ub - lb)) / (3 * sqrt(N));
	end

	% Initialize matrices and vectors of the CMA-ES
	pc = zeros(N,1);
	ps = zeros(N,1);
	B = eye(N,N);
	D = eye(N,N);
	C = B * D * (B * D)';

	% Initialize counters
	evalcount = 0;
	gencount = 0;

	% Statistics administration parameters
	stat.optimizer_name = 'CMA-ES';
	stat.run_status = 'Incomplete';
	estimated_stopeval = max(stopeval, stopgen * lambda);
	estimated_stopgen = max(stopgen, ceil(stopeval / lambda));
	stat.gencount = 0;
	stat.evalcount = 0;
	stat.x_opt = xmean;
	stat.f_opt = Inf;
	writeStats(stat.gencount, stat.f_opt, stat.x_opt, run_parameters.resultdir, run_parameters.seed);

	if (history_statistics)
		stat.evalvsgen = zeros(1, estimated_stopgen);
		stat.hist_x_opt = zeros(N, estimated_stopgen);
		stat.hist_f_opt = zeros(1, estimated_stopgen);
		stat.hist_x = zeros(N, estimated_stopeval);
		stat.hist_f = zeros(1, estimated_stopeval);
	end
	if (internal_parameter_statistics)
		stat.hist_sigma = zeros(1, estimated_stopgen);
		stat.hist_ps = zeros(N, estimated_stopgen);
		stat.hist_pc = zeros(N, estimated_stopgen);
		stat.hist_C = zeros(N, N, estimated_stopgen);
		stat.hist_B = zeros(N, N, estimated_stopgen);
		stat.hist_D = zeros(N, N, estimated_stopgen);
	end

	% If restart then load restart log logfile
	if (do_restart && exist(restart_log_logfile, 'file'))
		load(sprintf('%s',restart_log_logfile));
		stat.run_status = 'Incomplete';
	end

	% Evolution loop
	while ((stopeval == -1 || evalcount < stopeval) ...
		&& (stopgen == -1 || gencount < stopgen) ...
		&& stat.f_opt > minfitness)

		% Statistics administration
		gencount = gencount + 1;
		stat.gencount = gencount;
		if (internal_parameter_statistics)
			stat.hist_sigma(gencount) = sigma;
			stat.hist_ps(:,gencount) = ps;
			stat.hist_pc(:,gencount) = pc;
			stat.hist_C(:,:,gencount) = C;
			stat.hist_B(:,:,gencount) = B;
			stat.hist_D(:,:,gencount) = D;
		end

		% Generate and evaluate lambda offspring
		for k=1:lambda,
			zo(:,k) = randn(N,1);
			xo(:,k) = xmean + sigma * (B * D * zo(:,k));
			xo(:,k) = feval(bch_fct, xo(:,k), lb, ub);
		end

		[status output] = system(['"', fitnessfct, '" "', mat2str(xo), '" ', num2str(size(xo,1)), ' ', num2str(size(xo,2))]);
		fo = eval(output(strfind(output, '['):strfind(output, ']')));

		% Statistics administration
		for k=1:lambda,
			evalcount = evalcount + 1;
			stat.evalcount = evalcount;
			if (history_statistics)
				stat.hist_x(:,evalcount) = xo(:,k);
				stat.hist_f(evalcount) = fo(k);
			end
		end

		% Sort by fitness and compute weighted mean into xmean
		[sortf, sortindex] = sort(fo); % M I N I M I Z A T I O N
		zmean = zo(:,sortindex(1:mu)) * weights;

		% Compute xmean from zmean (Note that this is the safest way to compute xmean, as the xo values might have changed by the bch function
		xmean = feval(bch_fct, xmean + sigma * (B * D * zmean), lb, ub);

		% Cumulation: Update evolution paths
		ps = (1 - cs) * ps + sqrt(cs * (2 - cs) * mueff) * (B * zmean);
		hsig = norm(ps) / sqrt(1 - (1 - cs)^(2 * evalcount / lambda)) / chiN < 1.4 + 2/(N + 1);
		pc = (1 - cc) * pc + hsig * sqrt(cc * (2 - cc) * mueff) * (B * D * zmean);

		% Adapt covariance matrix C
		C = (1 - ccov) * C + ccov * (1 / mucov) * (pc * pc' + (1-hsig) * cc * (2 - cc) * C) + ccov * (1-1/mucov) * (B * D * zo(:,sortindex(1:mu))) * diag(weights) * (B * D * zo(:,sortindex(1:mu)))';

		% Adapt step size sigma
		sigma = sigma * exp((cs / damps) * (norm(ps) / chiN - 1));

		% Update B and D from C
		C = triu(C) + triu(C,1)';
		[B,D] = eig(C);
		D = diag(sqrt(diag(D)));

		% Statistics administration
		stat.f_opt = sortf(1);
		stat.x_opt = xo(:,sortindex(1));
		writeStats(stat.gencount, stat.f_opt, stat.x_opt, run_parameters.resultdir, run_parameters.seed);

		if (history_statistics)
			stat.evalvsgen(stat.gencount) = evalcount;
			stat.hist_x_opt(:,gencount) = stat.x_opt;
			stat.hist_f_opt(gencount) = stat.f_opt;
		end

		% Store log for restart
		if (restart_log_intermediate)
			save(sprintf('%s',restart_log_logfile), 'xmean', 'sigma', 'pc', 'ps', 'B', 'D', 'C', 'evalcount', 'gencount', 'stat');
		end

		% Report statistics
		if (report_intermediate)
			report_fct(stat, problem_specification, optimizer_parameters, run_parameters)
		end

	end

	% Complete statistics struct
	if (history_statistics)
		stat.evalvsgen = stat.evalvsgen(1:gencount);
		stat.hist_x = stat.hist_x(:,1:evalcount);
		stat.hist_f = stat.hist_f(1:evalcount);
		stat.hist_x_opt = stat.hist_x_opt(:,1:gencount);
		stat.hist_f_opt = stat.hist_f_opt(1:gencount);
	end
	if (internal_parameter_statistics)
		stat.hist_sigma = stat.hist_sigma(1:gencount);
		stat.hist_ps =  stat.hist_ps(:,1:gencount);
		stat.hist_pc = stat.hist_pc(:,1:gencount);
		stat.hist_C = stat.hist_C(:,:,1:gencount);
		stat.hist_B = stat.hist_B(:,:,1:gencount);
		stat.hist_D = stat.hist_D(:,:,1:gencount);
	end
	stat.run_status = 'Complete';

	% Store log
	if (restart_log_intermediate || restart_log_after_termination)
		save(sprintf('%s',restart_log_logfile), 'xmean', 'sigma', 'pc', 'ps', 'B', 'D', 'C', 'evalcount', 'gencount', 'stat');
	end

	% Plot statistics
	if (report_after_termination)
		report_fct(stat, problem_specification, optimizer_parameters, run_parameters)
	end
end

%==================================================================================================================================================

function value = readField(struct, fieldName, defaultValue)
	value = defaultValue;
	if (isfield(struct, fieldName))
		value = getfield(struct, fieldName);
	end
end


function [] = writeStats(gen, bestf, bestx, dir, seed)
	filename = [dir, '/run', num2str(seed), '.txt'];
	file = fopen(filename, 'a');
        fprintf(file, '%s | %s | %s\n', num2str(gen), num2str(bestf), mat2str(bestx));
	fclose(file); 
end
