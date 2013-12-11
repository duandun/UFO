function x = reflect_bc(x, lb, ub)
% x = reflection(x, lb, ub)
%   Reflects the vector x with the boundaries of the box
%   defined by lb and ub as reflection axes.
%
% Last modified: August 20, 2009

	xnorm = (x - lb) ./ (ub - lb);
	xnorm = (mod(abs(floor(xnorm)), 2) == 0) .* abs(xnorm - (floor(xnorm))) + (mod(abs(floor(xnorm)), 2) ~= 0) .* abs(1 - abs(xnorm - (floor(xnorm))));

	x = (x >= lb) .* (x <= ub) .* x + max((x < lb), (x > ub)) .* (lb + (ub - lb) .* xnorm);

end
