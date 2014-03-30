#!/usr/bin/env python
# -*- coding: utf-8 -*-

import Utils
import numpy as np


path = "../../jsonData/zenon_2013-12-26-20:1.json"
data = Utils.NMFUtils.readJson(path=path) 
data = Utils.NMFUtils.json2NpArray(data)		

W = data['W']
H = data['H']
V = data['V']

Wp = data['Wp']
Hd = np.dot(Wp, V)

Utils.NMFUtils.createCoefGraph(H, 1)
Utils.NMFUtils.showPlot()
