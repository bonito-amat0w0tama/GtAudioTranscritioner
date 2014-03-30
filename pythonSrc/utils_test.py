import numpy as np
import Utils
import pylab as plt 
import scipy.linalg as sl
import scipy.signal as sig
from NMFPloter import *

nu = Utils.NMFUtils
ploter = NMFPloter()

stpath = "../../jsonData/st/5st_2014-1-10-20:54.json"
st = nu.readJson(path=stpath)
st = nu.json2NpArray(st)

V = st['V']
W = st['W']
H = st['H']
SW = st['SW']
SH = st['SH']
Wp = sl.pinv(SW) 

#list = []
#for h in SH:
#    max = np.amax(h)
#    if max > 1.0:
#        list.append(h)
#
#nRows = len(list)
#nCols = SH.shape[1]
#A = np.zeros([nRows, nCols])
#print A
#
#for i in range(len(list)):
#    A[i,:] = list[i]
#
#indexArray = np.array(SH.shape[1], np.bool)
#for i in SH:
#    index = np.index(np.max(i)) 
#    indexArray[index] = True
#
#
#print indexArray

#for v in SH:

xs = np.arange(0, 3 * np.pi, 0.05)
#data = np.sin(xs) 
data = SW[:,0]


def findPeak(data, min=0):
    peak = []

    for i in range(len(data)):
        if i-1 > 0 and i+1 < len(data):
            pre = data[i-1]
            now = data[i] 
            next = data[i+1]
            if pre < now and now > next and now > min:
                peak.append(i)
    return peak

def findPeak2(data):
    peak = []
    d = np.diff(data)
    ran = range(len(d))

    for i in ran:
        if i+1 < len(d):
            now = d[i] 
            next = d[i+1]
            if now * next <= 0 and now > next:
                peak.append(i+1)
    return peak

min = np.max(data) * 0.1
peak = findPeak(data=data, min=min)

a = np.zeros(len(data))
list = []

for i in peak: 
    bai = 0.97 
    bai2 = 1.03
    for i in range(int(i*bai), int(i*bai2)+1):
        a[i] += 1
        list.append(i)
difflist = []

print peak
print list

b = np.zeros(len(data))

#counter = np.zeros(len(peak))
#counter = np.zeros(len(data))
counter = np.zeros(data.size, dtype=int)

for i in peak:
    for j in list:
        if j % i is 0:
            counter[i] += 1
    
print b.size
print len(b)

for i in range(len(counter)):
    if  counter[i] > 0:
        print str(i) + ":" + str(counter[i])

print np.argmax(counter) 
fs = 44100.0


#plt.axis([0.0, fs/2, 0, 50])
#naiki = int(fs/2)
#f = range(0, len(data), len(data) / naiki) 
#plt.plot(SW[:,0], marker='o')
#plt.ylim(ymin=min)

plt.show()
