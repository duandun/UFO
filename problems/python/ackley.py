#!/usr/bin/env python 
# -*- coding: utf-8 -*-

import sys
from numpy import *

def ackley(pop, N, numSol):
  c1 = 20
  c2 = 0.2
  c3 = 2*math.pi

  pop = pop[1:-1]
  N = int(N)
  numSol = int(numSol)

  rows = pop.split(';')
  pop = zeros((N,numSol)) 
  for i in range(0, len(rows)):
    pop[i,:] = map(float, rows[i].split())

  pop = pop.transpose()

  f = '['
  for i in range(0, numSol):
    f += str(-c1 * exp(-c2 * sqrt(1.0/N * sum(pop[i,:]**2))) - exp(1.0/N * sum(cos(c3 * pop[i,:]))) + c1 + e) + ';'
  f += ']'

  print f

if __name__ == '__main__':
  ackley(sys.argv[1], sys.argv[2], sys.argv[3])  


