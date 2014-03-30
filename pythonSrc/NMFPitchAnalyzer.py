#/usr/bin/env python
# -*- coding: utf-8 -*-

import numpy as np
from Utils import *
from NMFPloter import *
import scipy.linalg as sl
import pylab
#import NMFUtils

class NMFPitchAnalyzer:
    def __init__(self, fs=44100):
        self.fs = fs
        #self.nFFT = nFFT
        print "init"

    def findPeak(self, vec, min):
        peak = []
        for i in range(len(vec)):
            if 0 < i-1 and i+1 < len(vec):
                pre = vec[i-1]
                now = vec[i] 
                next = vec[i+1]
                if pre < now and now > next and now > min:
                    peak.append(i)
        return peak

    def findPeak2(data, min):
        peak = []
        d = np.diff(data)
        ran = range(len(d))

        for i in ran:
            if i+1 < len(d):
                now = d[i] 
                next = d[i+1]
                if now * next <= 0 and now > next and now >= min:
                    peak.append(i+1)
        return peak

    def createNewPeak(self, peak):
        newPeak = []
        bai = 0.95 
        bai2 = 1.05
        for i in peak: 
            for j in range(int(i*bai), int(i*bai2)+1):
                newPeak.append(j)
        return newPeak

    def findOvertone(self, peak, nFFT):
        overtone = np.zeros(nFFT, dtype=int)
        newPeak = self.createNewPeak(peak=peak)
        for i in peak:
            for j in newPeak:
                if j % i is 0:
                    overtone[i] += 1
        return overtone

    def printOvertone(self, overtone):
        for i in range(overtone.size):
            if overtone[i] > 0:
                print "%d: %d" %(i, overtone[i])

    def findInterval(self, peak, newPeak, nFFT):
        counter = np.zeros(nFFT, dtype=int)
        for i in peak:
            for j in newPeak:
                if j % i is 0:
                    counter[i] += 1

        interval = np.argmax(counter)
        return interval

    def analyzePitch(self, vec, min):
        naiki = 44100 / 2
        nFFT = vec.size

        peak = self.findPeak(vec, min)
        newPeak = self.createNewPeak(peak) 
        interval = self.findInterval(peak, newPeak, nFFT)

        pitch = interval * naiki / nFFT 
        return pitch

    def deleteNoise(self, W, H):
        #noiseTh = 20
        # ノイズ判定における倍音のしきい値
        noiseTh = 14 
        noiseBool = np.zeros((W.shape[1],), dtype='bool')

        # ノイズの判定
        for i in range(W.shape[1]):
            Wvec = W[:,i] 
            nFFT = Wvec.size
            min = np.max(Wvec) * 0.1
            peak = self.findPeak(Wvec, min)
            overtone = self.findOvertone(peak, nFFT)

            numOt = len(overtone[overtone>0])
            maxOt = np.argmax(overtone[overtone>0])

            # ノイズの条件
            if (numOt < 2) or (noiseTh < numOt) or maxOt > 0:
                noiseBool[i] = False 
            else:
                noiseBool[i] = True

        newW = np.zeros((W.shape[0], noiseBool[noiseBool==True].size))
        newH = np.zeros((noiseBool[noiseBool==True].size, H.shape[1]))
        index = 0

        # ノイズ判定されたものを削除
        for i in range(noiseBool.size):
            if noiseBool[i]:
                newW[:,index] = W[:,i]
                newH[index,:] = H[i,:]
                index += 1

        newH = pf.deleteIdou(newH)

        # 同じ音をまとめる処理
        Hbool = np.zeros((newH.shape[0],), dtype='bool')
        i = 0
        while i < newH.shape[0]:
            if i+1 < newH.shape[0]:
                val = np.dot(newH[i,:], newH[i+1,:])
                if val >= 5.0:
#                    newH[i,:] = newH[i,:] + newH[i+1,:]
#                    newH[i+1,:] = np.zeros((newH.shape[1],))
#                    newW[:,i] = newW[:,i] + newW[:,i+1]
#                    newW[:,i+1] = np.zeros((newW.shape[0],))
                    Hbool[i] = True
                    Hbool[i+1] = False 
                    i += 2
                else:
                    Hbool[i] = True 
                    i += 1
            else:
                break

        destW = np.zeros((W.shape[0], Hbool[Hbool==True].size))
        destH = np.zeros((Hbool[Hbool==True].size, H.shape[1]))

        index = 0
        for i in range(Hbool.size):
            if Hbool[i] and not Hbool[i+1]:
                destW[:,index] = newW[:,i] + newW[:,i+1]
                destH[index,:] = newH[i,:] + newH[i+1,:]
                index += 1 
            elif Hbool[i]:
                destW[:,index] = newW[:,i]
                destH[index,:] = newH[i,:]
                index += 1

        return destW, destH

    def deleteIdou(self, vecs):
        Th = 3
        bool = np.zeros((vecs.shape[0],), dtype='bool')
        for i in range(0, vecs.shape[0], 1):
            vec = vecs[i,:]
            min = np.max(vec) * 0.3
            p = pf.findPeak(vec, min)
            print len(p)
            if len(p) > Th:
                bool[i] = False
            else:
                bool[i] = True

        dest = np.zeros((bool[bool==True].size, vecs.shape[1]))

        inx = 0
        for i in range(len(bool)):
            if bool[i]:
                dest[inx,:] = vecs[i,:]
                inx += 1
        return dest


    def deleteNoise2(self, W, H):
        noiseTh = 20
        Wbool = np.zeros((W.shape[1],), dtype='bool')

        # ノイズの判定
        for i in range(W.shape[1]):
            Wvec = W[:,i] 
            nFFT = Wvec.size
            min = np.max(Wvec) * 0.1
            peak = self.findPeak(Wvec, min)
            overtone = self.findOvertone(peak, nFFT)

            numOt = len(overtone[overtone>0])
            maxOt = np.argmax(overtone[overtone>0])

            # ノイズの条件
            if (numOt < 2) or (noiseTh < numOt) or maxOt > 0:
                Wbool[i] = False 
            else:
                Wbool[i] = True

        newW = np.zeros((W.shape[0], Wbool[Wbool==True].size))
        newH = np.zeros((Wbool[Wbool==True].size, H.shape[1]))
        index = 0

        # ノイズ判定されたものを削除
        for i in range(Wbool.size):
            if Wbool[i]:
                newW[:,index] = W[:,i]
                newH[index,:] = H[i,:]
                index += 1

        # 同じ音をまとめる処理
        Hbool = np.zeros((newH.shape[0],), dtype='bool')
        i = 0

#        while i < newH.shape[0]:
#            if i+1 < newH.shape[0]:
#                val = np.dot(newH[i,:], newH[i+1,:])
#                if val >= 5.0:
##                    newH[i,:] = newH[i,:] + newH[i+1,:]
##                    newH[i+1,:] = np.zeros((newH.shape[1],))
##                    newW[:,i] = newW[:,i] + newW[:,i+1]
##                    newW[:,i+1] = np.zeros((newW.shape[0],))
#                    Hbool[i] = True
#                    Hbool[i+1] = False 
#                    i += 2
#                else:
#                    Hbool[i] = True 
#                    i += 1
#            else:
#                break

        while i < newH.shape[0]:
            for j in range(i+1, newH.shape[0]):
                val = np.dot(newH[i,:], newH[j,:])
                if val >= 5.0:
                    Hbool[i] = True
                    Hbool[i+1] = False 
                    i += 2
                else:
                    Hbool[i] = True 
                    i += 1
            else:
                break

        destW = np.zeros((W.shape[0], Hbool[Hbool==True].size))
        destH = np.zeros((Hbool[Hbool==True].size, H.shape[1]))

        index = 0
        for i in range(Hbool.size):
            if Hbool[i] and not Hbool[i+1]:
                destW[:,index] = newW[:,i] + newW[:,i+1]
                destH[index,:] = newH[i,:] + newH[i+1,:]
                index += 1 
            elif Hbool[i]:
                destW[:,index] = newW[:,i]
                destH[index,:] = newH[i,:]
                index += 1

        return destW, destH

if __name__ == "__main__":
    pf = NMFPitchAnalyzer(fs=44100)

    nu = NMFUtils()
    ploter = NMFPloter()

    stpath = "../../jsonData/st/5st_2014-1-10-20:54.json"
    #stpath = "../../jsonData/st/6st_2014-1-10-22:45.json"
#    st1path = "../../jsonData/st/short/1st.json"
#    st2path = "../../jsonData/st/short/2st.json"
#    st3path = "../../jsonData/st/short/3st.json"
#    st4path = "../../jsonData/st/short/4st.json"
#    st5path = "../../jsonData/st/short/5st.json"
#    st6path = "../../jsonData/st/short/6st.json"
    st1path = "../../jsonData/st/0124/1st.json"
    st2path = "../../jsonData/st/0124/2st.json"
    st3path = "../../jsonData/st/0124/3st.json"
    st4path = "../../jsonData/st/0124/4st.json"
    st5path = "../../jsonData/st/0124/5st.json"
    st6path = "../../jsonData/st/0124/6st.json"
#    st1path = "../../jsonData/st/0208/1st.json"
#    st2path = "../../jsonData/st/0208/2st.json"
#    st3path = "../../jsonData/st/0208/3st.json"
#    st4path = "../../jsonData/st/0208/4st.json"
#    st5path = "../../jsonData/st/0208/5st.json"
#    st6path = "../../jsonData/st/0208/6st.json"
    st1path = "../../jsonData/st/0210/1st.json"
    st2path = "../../jsonData/st/0210/2st.json"
    st3path = "../../jsonData/st/0210/3st.json"
    st4path = "../../jsonData/st/0210/4st.json"
    st5path = "../../jsonData/st/0210/5st.json"
    st6path = "../../jsonData/st/0210/6st.json"

    st1path = "../../jsonData/st/0212/1st.json"
    st2path = "../../jsonData/st/0212/2st.json"
    st3path = "../../jsonData/st/0212/3st.json"
    st4path = "../../jsonData/st/0212/4st3.json"
    st5path = "../../jsonData/st/0212/5st3.json"
    st6path = "../../jsonData/st/0212/6st3.json"

    #st1path = "../../jsonData/st/0212/d2st.json"
    path = st1path
    st = nu.readJson(path=path)
    st = nu.json2NpArray(st)

    V = st['V']
    W = st['W']
    H = st['H']
    SW = st['SW']
    SH = st['SH']
    Wp = sl.pinv(SW) 

#    W = st['W']
#    H = st['H']
    vec = SW[:,0]

    st1 = ['E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B', 'C', 'C#', 'D', 'D#']
    st2 = ['B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#', 'A', 'A#']
    st3 = ['G', 'G#', 'A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#']
    st4 = ['D', 'D#', 'E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B', 'C', 'C#']
    st5 = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#']
    st6 = ['E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B', 'C', 'C#', 'D', 'D#']
    pitchList = []
    pre = None 

#    dW, dH = pf.deleteNoise(SW, SH)
#
#    stName = st1
#
#    for i in range(SW.shape[1]):
#        vec = SW[:,i] 
#        min = np.max(vec) * 0.1
#        now = pf.analyzePitch(vec, min)
#        if pre is None:
#            pre = now 
#        peak = pf.findPeak(vec, min)
#        overtone = pf.findOvertone(peak, vec.size)
#        pf.printOvertone(overtone)
#        numOt = len(overtone[overtone>0])
#        print numOt 
#        print stName[i%12] + str(i/12+1) + ":" + str(now) + ":" + "\n"
#        #print "%f" % (np.max(vec)
#
#    print "================"
#
#    for i in range(dW.shape[1]):
#        vec = dW[:,i] 
#        min = np.max(vec) * 0.1
#        now = pf.analyzePitch(vec, min)
##        if pre is None:
##            pre = now 
#        peak = pf.findPeak(vec, min)
#        overtone = pf.findOvertone(peak, vec.size)
#        pf.printOvertone(overtone)
#        numOt = len(overtone[overtone>0])
#        print numOt 
#        print stName[i%12] + str(i/12+1) + ":" + str(now) + "\n"
#        #print "%f" % (np.max(vec))
#
#    for i in range(0, dH.shape[0], 1):
#        try:
#            print "%s*%s:%f" %(stName[i%12], stName[(i+1)%12], np.dot(dH[i,:],dH[i+1,:]))
#        except IndexError as e:
#            print "hello"
#


    dels1 = [14, 17, 21, 23, 25, 26, 29, 31, 32, 19, 28, 34]
    dels2 = [16, 18, 20, 22, 23, 25, 27, 28, 29, 31, 32, 34]
    dels3 = [12, 16, 18, 19, 21, 22, 25, 26, 28, 29, 31, 33]
    dels4 = [16, 18, 20, 21, 23, 24, 26, 27, 29, 31, 32, 34]
    dels5 = [13, 16, 18, 19, 21, 23, 25, 27, 29, 30 ,32, 33]
    dels6 = [11, 15, 17, 18, 20, 21, 23, 24, 26, 30 ,31, 33]

    dels1 = [12, 16, 18, 21, 26]
    dels2 = [13, 18, 20, 23, 26]
    dels3 = [8, 16, 19, 22, 26]
    dels4 = []
    dels5 = []
    dels6 = []
    dW, dH = nu.deleteVec(W=SW, H=SH, dels=dels1)
    #dW, dH = nu.deleteVec(W=SW, H=SH, dels=dels6)
    #dW, dH = nu.deleteVec(W=SW, H=SH, dels=dels3)
    #dW, dH = nu.deleteVec(W=SW, H=SH, dels=dels4)
    #dW, dH = nu.deleteVec(W=SW, H=SH, dels=dels5)
    #dW, dH = nu.deleteVec(W=SW, H=SH, dels=dels6)

    #ploter.createBasisGraph(data=SW)
    #ploter.createBasisGraph(data=dW)
    #ploter.createCoefGraph(data=SH)
    #ploter.createCoefGraph(data=dH)


    
#    dW, dH = pf.deleteNoise(SW, SH)
#
    #ploter.createBasisGraph(data=tes)
    ploter.createBasisGraph(data=dW)
    ploter.createCoefGraph(data=dH)
    #ploter.createBasisGraph(data=SW)
    #ploter.createCoefGraph(data=SH)

    dWp = sl.pinv(dW)
    data = {"dWp":dWp.tolist()}
    nu.writeDataToJson("d6st", data)

    pylab.show()


