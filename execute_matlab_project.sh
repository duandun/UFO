#!/bin/bash

if [ $# -lt 2 ]; then
  echo "Usage: $0 <matlab> <project_json>"
  exit
fi

chmod -R a+x problems/*/

$1 -nodisplay -nodesktop -nojvm -nosplash -r "addpath('optimizers/MATLAB/'), ufo_matlab_optimizer('$2'), quit" > /dev/null

