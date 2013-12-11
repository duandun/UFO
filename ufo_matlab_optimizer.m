function [] = ufo_matlab_optimizer(project_json)
	addpath('json/jsonlab');  

	project_specification = loadjson(project_json);
	problem_specification = loadjson(project_specification.problem_json);
	optimizer_parameters  = loadjson(project_specification.optimizer_json);
	run_parameters        = loadjson(project_specification.run_json);
 
	currentdir = cd;
	currentdir = strrep(currentdir, '\', '/');
	problem_specification.path = [currentdir, '/',  problem_specification.path];
	optimizer_parameters.path  = [currentdir, '/',  optimizer_parameters.path];
	addpath(optimizer_parameters.path);

	if (run_parameters.seed == -1)
		run_parameters.seed = floor((datenum(clock) - datenum('1-1-2012')) * 100000);
	end
	randn('seed', run_parameters.seed);
	rand('seed', run_parameters.seed);
    
	run_parameters.resultdir = strtok(project_json,'.');
	mkdir(run_parameters.resultdir);

	feval(optimizer_parameters.routine, problem_specification, optimizer_parameters, run_parameters);
end
