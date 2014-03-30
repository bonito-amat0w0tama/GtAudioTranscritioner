#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pylab as plt
import numpy as np
import nimfa

class NMFPloter:
	@staticmethod
	def createBasisGraph(data, nFig):
		plt.figure(nFig)
		plt.suptitle('Basis')
		nBase = data.shape[1]
		for i in range(nBase):
			# Because Index of graph is start by 1, The Graph index is i + 1.
			plt.subplot(nBase, 1, i + 1)
			plt.tick_params(labelleft='off', labelbottom='off')
			plt.plot(data[:,i])
		# Beacuse I want to add lable in bottom, xlabel is declaration after loop.
		plt.tick_params(labelleft='off', labelbottom='on')
		plt.xlabel('frequency [Hz]')

	@staticmethod
	def createCoefGraph(data, nFig):
		plt.figure(nFig)
		plt.suptitle('Coef')
		nBase = data.shape[0]
		for i in range(nBase):
			plt.subplot(nBase, 1, i + 1)
			plt.tick_params(labelleft='off', labelbottom='off')
			# FIXME: Arguments of X
			plt.plot(data[i,:])
		# Beacuse I want to add lable in bottom, xlabel is declaration after loop.
		plt.tick_params(labelleft='off', labelbottom="on")
		plt.xlabel('time [ms]')


if __name__ == "__main__":
	X = np.random.random_sample(1000).reshape(20, 50)

	fctr = nimfa.mf(X, seed = "random_vcol", method = "nmf", rank = 40, mat_iter = 50)
	fctrRes = nimfa.mf_run(fctr)

	W = fctrRes.basis()
	H = fctrRes.coef()
	print W
	print H
	NMFPloter.createBasisGraph(W, 1)	
	NMFPloter.createCoefGraph(H, 2)
	plt.show()