#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pylab as plt
import numpy as np
import nimfa
import json
import datetime
import os



class NMFPloter:
    
    #nFig = 0
    def __init__(self):
        self.nFig = 0
        self.st5 = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#']

    def createBasisGraph(self, data):
        plt.figure(self.nFig)
        plt.suptitle('Basis')
        nBase = data.shape[1]
        # The cols number of subplot is 
        nSubCols = nBase / 10
        if nSubCols > 0:
            if nBase % 2 == 0:
                nSubRows = nBase / nSubCols
            else:
                nSubRows = nBase / nSubCols + 1
        else:
            nSubRows = nBase 
            nSubCols = 1
        # freqList = np.fft.fftfreq(513, d = 1.0 / 44100)

        for i in range(nBase):
            nowFig = self.nFig + (i / nSubRows) + 1
            # Because Index of graph is start by 1, The Graph index start from i + 1.
            plt.subplot(nSubRows, nSubCols, i + 1)
            plt.tick_params(labelleft='off', labelbottom='off')

            # FIXME
            #plt.ylabel(self.st5[i%12] + str(i/12  + 1))
            plt.ylabel(str(i))
            plt.plot(data[:,i])
        # Beacuse I want to add lable in bottom, xlabel is declaration after loop.
        plt.tick_params(labelleft='off', labelbottom='on')
        plt.xlabel('frequency [Hz]')

        #self.nFig += nowFig
        self.nFig += 1 

    def createCoefGraph(self, data, lim=True, ymin=0):
        plt.figure(self.nFig)
        plt.suptitle('Coef')
        nBase = data.shape[0]

        if nBase > 12:
            # 一枚のグラフにつき12行,２列表示
#            nSubRows = 12 
#            nSubCols = 2 
            nSubRows = nBase 
            nSubCols = 1 
            endFig = (nBase / 24) + 1
        elif nBase is 12:
            nSubRows = 12
            nSubCols = 1 
        else:
            nSubRows = nBase 
            nSubCols = 	1

        # サンプリング周波数とシフト幅によって式を変える必要あり
        timeLine = [i * 1024 / 8000.0 for i in range(data.shape[1])]
        # print len(timeLine)
        for i in range(nBase):
            nowFig = self.nFig + (i / nSubRows) + 1
            #plt.figure(nowFig)
            plt.subplot(nSubRows, nSubCols, i + 1)
            plt.tick_params(labelleft='off', labelbottom='off')
            # FIXME: Arguments of X
            # plt.plot(timeLine, data[i,:])
            if lim:
                plt.ylim(ymin=ymin)
            #FIXME:
            #plt.ylabel(str(i) + ":" + self.st5[i%12] + str(i/12  + 1))
            plt.ylabel(str(i))
            plt.plot(data[i,:])
        # Beacuse I want to add lable in bottom, xlabel is declaration after loop.
        plt.tick_params(labelleft='off', labelbottom="on")
        plt.xlabel('time [ms]')

        #self.nFig += nowFig
        self.nFig += 1 

    def createComparedBasisGraph(self, data1, data2):
        plt.figure(self.nFig)
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


    def createActivationGraph(self, data, nFig):
        plt.figure(nFig)
        plt.suptitle('Activation')
        nBase = data.shape[0]
        nSubCols = nBase / 10
        if nSubCols > 0:
            nSubRows = nBase / nSubCols
        else:
            nSubRows = nBase 
            nSubCols = 	1
        # print data.shape

        # サンプリング周波数とシフト幅によって式を変える必要あり
        #timeLine = [i * 1024 / 8000.0 for i in range(data.shape[1])]
        # print len(timeLine)
        for i in range(nBase):
            plt.subplot(nSubRows, nSubCols, i + 1)
            plt.tick_params(labelleft='off', labelbottom='off')
            # FIXME: Arguments of X
            # plt.plot(timeLine, data[i,:])
            plt.plot(data[i])
        # Beacuse I want to add lable in bottom, xlabel is declaration after loop.
        plt.tick_params(labelleft='off', labelbottom="on")
        plt.xlabel('time [ms]')

    def createComparedCoefGraph(self, data1, data2, nFig):
        plt.figure(nFig)
        plt.suptitle('Coef')
        nBase = data1.shape[0]
        nSubCols = nBase / 10
        nSubRows = nBase / nSubCols

        # サンプリング周波数とシフト幅によって式を変える必要あり
        timeLine = [i * 1024 / 8000.0 for i in range(data1.shape[1])]

        for i in range(nBase): 
            plt.subplot(nSubRows, nSubCols, i + 1)
            plt.tick_params(labelleft='off', labelbottom='off')
            # FIXME: Arguments of X
            plt.plot(timeLine, data1[i,:], 'r', timeLine, data2[i,:], 'b')
        # Beacuse I want to add lable in bottom, xlabel is declaration after loop.
        plt.tick_params(labelleft='off', labelbottom="on")
        plt.xlabel('time [ms]')

    def sortBasisAndCoef(self, W, H):
        list = [] 
        for i in range(H.shape[0]):
            max = np.amax(H[i,:])
            for j in range(H.shape[1]): 
                if H[i,j] == max:
                    # タプル(最大値がでるインデックス, 対応する行数)
                    # ソートしやすいように最大値がでるインデックスを前にした
                    list.append((j,i))
                    # 見つかったら抜ける
                    break
        # 最大振幅が見つかったインデックスを昇順でソート
        list.sort()

        # ソートした行列を格納するためにゼロ行列を作成
        sortedW = np.zeros(W.shape)
        sortedH = np.zeros(H.shape)

        num = 0
        # 行と列の入れ替え 
        for k in list:
            sortedH[num,:] = H[k[1],:]
            sortedW[:,num] = W[:,k[1]]
            num += 1
        return sortedW, sortedH

    def readJson(self, path):
        f = open(path)
        data = json.load(f)
        return data

    def list2NpArray(self, data):
        defData = np.asarray(data)
        return defData


    def	json2NpArray(self, json):
        dic = {}
        for k,v in json.iteritems():
            dic[k] = NMFUtils.list2NpArray(v)
        return dic	

    def printHello(self):
        print "hello"

    def showPlot(self):
        plt.show()

    def putMaxIndex(self):
        max = np.amax(arr)
        index = 0
        for i in arr:
            if max == i:
                return index
            index += 1
        return 0

    def putAllMaxIndex(self, mat):
        for arr in mat:
            print NMFUtils.putMaxIndex(arr)

    def dbug(self, data):
        print data
    
