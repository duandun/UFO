function [] = ackley(pop, N, numSol)
  c1 = 20;
  c2 = 0.2;
  c3 = 2*pi;

  pop = eval(pop)';
  N = str2num(N);
  numSol = str2num(numSol);

  f = '[';
  for i=1:numSol,
    f = [f, num2str(-c1 * exp(-c2 * sqrt(sum(pop(i,:) .^ 2, 2) / N)) - exp(sum(cos(c3 * pop(i,:)), 2) / N) + c1 + exp(1)), ';'];
  end
  f = [f, ']'];

  fprintf('%s\n', f);
end
