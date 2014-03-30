#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pylab as plt
import numpy as np
import nimfa
import json


class NMFPloter:
	@staticmethod
	def createBasisGraph(data, nFig):
		plt.figure(nFig)
		plt.suptitle('Basis')
		nBase = data.shape[1]
		# The cols number of subplot is 
		nSubCols = nBase / 10
		nSubRows = nBase / nSubCols
		for i in range(nBase):
			# Because Index of graph is start by 1, The Graph index is i + 1.
			plt.subplot(nSubRows, nSubCols, i + 1)
			plt.tick_params(labelleft='off', labelbottom='off')
			plt.plot(data[:,i])
		# Beacuse I want to add lable in bottom, xlabel is declaration after loop.
		plt.tick_params(labelleft='off', labelbottom='on')
		plt.xlabel('frequency [Hz]')

	def createComparedBasisGraph(data1, data2, nFig):
		plt.figure(nFig)
		plt.suptitle('Basis')
		nBase = data1.shape[1]
		for i in range(nBase):
			# Because Index of graph is start by 1, The Graph index is i + 1.
			plt.subplot(nBase/4, 4, i + 1)
			plt.tick_params(labelleft='off', labelbottom='off')
			plt.plot(data1[:,i], 'r', data2[:,i], 'b')
		# Beacuse I want to add lable in bottom, xlabel is declaration after loop.
		plt.tick_params(labelleft='off', labelbottom='on')
		plt.xlabel('frequency [Hz]')

	@staticmethod
	def createCoefGraph(data1, data2, nFig):
		plt.figure(nFig)
		plt.suptitle('Coef')
		nBase = data1.shape[0]
		for i in range(nBase):
			plt.subplot(nBase / 4, 4, i + 1)
			plt.tick_params(labelleft='off', labelbottom='off')
			# FIXME: Arguments of X
			plt.plot(data1[i,:], 'r', data2[i,:], 'b')
		# Beacuse I want to add lable in bottom, xlabel is declaration after loop.
		plt.tick_params(labelleft='off', labelbottom="on")
		plt.xlabel('time [ms]')



if __name__ == "__main__":
	X = np.random.random_sample(1000).reshape(20, 50)

	fctr = nimfa.mf(X, seed = "random_vcol", method = "nmf", rank = 40, mat_iter = 50)
	fctrRes = nimfa.mf_run(fctr)

	W = fctrRes.basis()
	W2 = 3 * W
	H = fctrRes.coef()
	H2 = 3 * H
	# print W
	# print H

	f = open("../jsonData/2013-12-12-22:15_test.json")
	data = json.load(f)

	W = np.asarray(data['W'])
	H = np.asarray(data['H'])
	V = np.asarray(data['V'])

	Wp = np.asarray(data['Wp'])
	Hd = np.dot(Wp, V)


	NMFPloter.createBasisGraph(W, 1)
	#NMFPloter.createCoefGraph(H, Hd, 2)
	plt.show()