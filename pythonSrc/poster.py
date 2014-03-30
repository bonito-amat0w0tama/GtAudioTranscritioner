#/usr/bin/env python
# -*- coding: utf-8 -*-

import numpy as np
from Utils import *
from NMFPloter import *
import scipy.linalg as sl
import pylab

if __name__ == "__main__":
    nu = NMFUtils()
    ploter = NMFPloter()

    #stpath = "../../jsonData/AmScale_2014-1-9-21:16.json"
    stpath = "../../jsonData/am_2014-1-7-21:36.json"
    wavpath= "../../jsonData/cut.json"

    path = stpath
    st = nu.readJson(path=path)
    st = nu.json2NpArray(st)

    cut = nu.readJson(path=wavpath)
    cut = nu.json2NpArray(cut)

    V = st['V']
    W = st['W']
    H = st['H']
    SW = st['SW']
    SH = st['SH']
    Wp = sl.pinv(SW) 

    cut = cut['cut']

    vec = np.dot(Wp, cut)

    #ploter.createBasisGraph(data=tes)
    #ploter.createBasisGraph(data=SW)
    ploter.createCoefGraph(data=vec)
    #ploter.createBasisGraph(data=SW)
    #ploter.createCoefGraph(data=SH)

    pylab.show()

    #dWp = sl.pinv(dW)
    #data = {"dWp":dWp.tolist()}
    #nu.writeDataToJson("d6st", data)
