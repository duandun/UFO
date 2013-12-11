

Execute matlab project:
./execute_matlab_project.sh /auto_mnt/appl/MATHWORKS_R2010B/bin/matlab project__Ackley10D__CMAES_rodeolib.json




Communication between components:

########## Problem: 

---> input string <--- 

(recieved from standard input, stdin)

"path/problem_exe" "[sol_0(0) sol_1(0);sol_0(1) sol_1(1)]" dimension number_of_solutions

example:
individual 1 = [1;4;7;10]
individual 2 = [2;5;8;11]
...
"/home/ereehuis/Desktop/UFO/problems/MATLAB/python/ackley.py" "[1 2 3;4 5 6;7 8 9;10 11 12]" 4 3 


---> output string <--- 

(outputted to standard output, stdout)

"[score_0;score_1;score_2;]"


########## Optimizer: 

---> output file <---

project_name/run[seed].txt:

[gennumber] | [bestf] | [bestx]



