#!/usr/bin/env python
# -*- coding: utf-8 -*-

# 受け取るバッファはStr型

import socket
import sys
import struct 
import numpy as np
import scipy.sparse as sp
import nimfa
import os
import json
import datetime

class Junk_sendMat():
    def __init__(self, host, port):
        self.stack = []
        self.byteSizeInt = 4
        self.byteSizeHeader = 4
        self.host = host
        self.port = port

        self.recvSum = 0
        self.sendSum = 0


        self.clientsock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.clientsock.connect((host,port))

    def connectServer(self):
        #AF_INET:IPv4 インターネット・プロトコル
        #SOCK_STREAM:TCP/IPを用いたSTREAM型のソケット
        self.clientsock = socket.socket(socket.AF_INET, 
                socket.SOCK_STREAM)
        #host,portでバインドする
        self.clientsock.bind((self.host,self.port))

    # FIXME: name of clientsock
    def readHeader(self, clientsock):
        # bufferSize = 4
        header = clientsock.recv(self.byteSizeHeader)
        self.recvSum += self.byteSizeHeader
        return str(header)

    def readInt(self, clientsock):
        # BUFFERSIZE = 4
        #print "buff"
        buff = clientsock.recv(self.byteSizeInt)
        self.recvSum += self.byteSizeInt
        if buff == '':
            intBuffer = 0
        else:
            # FIXME:variable name 
            intBuffer = struct.unpack('i', buff)[0]
        return intBuffer

    def getMatrix(self, rows, cols, size, clientsock):
        # rows, cols の8バイト分マイナス
        dataSize = size - 8
        matrix = np.zeros((rows, cols))
        for i in range(rows):
            for j in range(cols):
                matrix[i,j] = struct.unpack('f', clientsock.recv(4))[0]
                self.recvSum += 4
        return  matrix

    def pushMatrix(self, matrix):

        buff = ''
        head = 'data'
        rows = matrix.shape[0]
        cols = matrix.shape[1]
        size = rows * cols * 4 + 8

        buff = struct.pack('cccciii', head[0], head[1], head[2], head[3], size, rows, cols)

        list = []
        for i in range(rows):
            for j in range(cols):
                # print type(matrix[i,j])
                matVal = matrix[i,j]

                val =  struct.pack('f', matVal)
                uVal = struct.unpack('f', val)
                buff = buff + val

                list.append(uVal)

        print "----------"
        print "PushMatrix"

        self.printUnpackMatrix(buff)
        self.clientsock.send(buff)
        self.sendSum += len(buff)

        print "sendSum:%d" % (self.sendSum)
            
    def sendError(self):
        head = "eror"
        buff = struct.pack("cccc", head[0], head[1], head[2], head[3])
        self.clientsock.send(buff)
        self.sendSum += len(buff)
        print "sendedError"

    # FIXME: メソッドの設計(clientsockなど）
    def popTcp(self):
        stack = []
        requestCount = 1
        head = ''

        head = self.readHeader(self.clientsock)
        size = self.readInt(self.clientsock)
        self.printSize(size)
        rows = self.readInt(self.clientsock)
        cols = self.readInt(self.clientsock)
        self.printRowsAndCols(rows, cols)
        matrix = self.getMatrix(rows, cols, size, self.clientsock)
        self.printMatrix(matrix)
        self.stack.append(matrix)

        print "recvSum:%d" % (self.recvSum)

            #elif head is 'end ' or size == 0:
                #break

    def push(self, matrix):
        print "----"
        print "push"
        print "----"
        self.stack.append(matrix)
        self.printStack()
        # ここでPushMatrixしたことがバグの原因
        # self.pushMatrix(matrix)

    def pop(self):
        print "---"
        print "pop"
        print "---"
        self.printStack()
        return self.stack.pop()

    # FIXME: length is bad
    def convertBinaryToString(self, buff, length):
        head = ''
        for i in range(length):
            head = head+ struct.unpack('c', buff[i])[0]
        return head

    # FIXME: unncode
    def convertBinaryToMatirx(self, buff, rows, cols, begin):
        matrix = np.zeros((rows, cols))
        end = begin + 4

        for i in range(rows):
            for j in range(cols):
                matrix[i,j] = struct.unpack('f', buff[begin:end])[0]
                begin += 4
                end += 4

        print matrix

        #for i in range(matrix.shape[0]):
        #    for j in range(matrix.shape[1]):
        #        print matrix[i,j]

    def getPseudoInverseMatrix(self, mat):
        return np.linalg.pinv(mat)

    def printUnpackMatrix(self, buff):
        print "------------"
        print 'unpackMatrix'
        print "------------"
        head = self.convertBinaryToString(buff, 4)
        size = struct.unpack('i', buff[4:8])[0]
        rows = struct.unpack('i', buff[8:12])[0]
        cols = struct.unpack('i', buff[12:16])[0]
        self.printHeader(head)
        self.printSize(size)
        self.printRowsAndCols(rows, cols)
        self.convertBinaryToMatirx(buff, rows, cols, 16)
        # print struct.unpack('cccciiiffff', buff)

    def printMatrix(self, matrix):
        print "data ->"
        print matrix

    def printRowsAndCols(self, rows, cols):
        print "Rows -> %d" % (rows)
        print "Cols -> %d" % (cols)

    # FIXME: うんこーど
    def printStack(self):
        print "stack_length -> %d" % (len(self.stack))
        print "Values in Stack"
        for i in range(len(self.stack)):
            print "%d -> " % (i+1)
            print self.stack[i]
        print "-------------"

    def printSize(self, size):
        print "Size -> %d" % (size)

    def printHeader(self, header):
        print "Header -> %s" % (header)

    def printCode(self, code):
        print 'Code->'
        print '%s' % (code)

    def printRequestCount(self, count):
        print "------------------"
        print "RequestCount -> %d" % (count)
        print "------------------"

    def execCode(self, code):
        print '----execute----'
        exec code
        print '----end--------'

    def nmfMatrix(self, V):
        print "---"
        print "NMF"
        print "---"

        V = np.array(V)
        print "Target matrix"
        print V.shape[0]
        print V.shape[1]
        print V
        
        
#         X = sp.rand(V.shape[0], V.shape[1], density=1).tocsr()
        # NMFの際の、基底数やイテレーションの設定
        rank = 10
        maxIter = 20
        method = "lsnmf"
        
#         initiarizer = nimfa.methods.seeding.random_vcol.Random_vcol()
        initiarizer = nimfa.methods.seeding.random.Random()
        initW, initH = initiarizer.initialize(V, rank, {})
        
        date = datetime.datetime.today()
        filePath = "../jsonData/" + "NMF_result" + str(date)

        fctr = nimfa.mf(V, seed = 'random_vcol', method = 'lsnmf', rank = 10, max_iter = 10)
        # fctr = nimfa.mf(V, method = "lsnmf", rank = rank, max_iter = maxIter, W = initW, H = initH)
        fctr_res = nimfa.mf_run(fctr)

        W = fctr_res.basis()
        print "Basis matrix"
        print "test"
        print W.shape[0]
        print W.shape[1]
        print W
        H = fctr_res.coef()
        print "Coef"
        print H.shape[0]
        print H.shape[1]
        print H

#         print "Estimate"
#         print np.dot(W, H)

#         print 'Rss: %5.4f' % fctr_res.fit.rss()
#         print 'Evar: %5.4f' % fctr_res.fit.evar()
#         print 'K-L divergence: %5.4f' % fctr_res.distance(metric = 'kl')
#         print 'Sparseness, W: %5.4f, H: %5.4f' % fctr_res.fit.sparseness()

        sm = fctr_res.summary()
        print "Rss: %8.3f" % sm['rss']
        # Print explained variance.
        print "Evar: %8.3f" % sm['evar']
        # Print actual number of iterations performed
        print "Iterations: %d" % sm['n_iter']

        # # ファイルが存在しない場合のみ、Jsonファイルを生成
        # if not os.path.isfile(filePath):
        #     file = open(filePath, "w")
        #     data = {
        #             "initW": initW,
        #             "initH": initH,
        #             "rank": rank,
        #             "matIter": maxIter,
        #             "method": method,
        #             "factr_res": fctr_res
        #             }
        #     json.dump(data, file)
        #     file.close()

        return W, H

    def createZeroMatrix(self, rows, cols):
        return np.zeros([rows, cols])

    def pushCode(self, code):
        buff = ""
        buff = struct.pack("cccc", 'c', 'o', 'd', 'e')

        buff += struct.pack('i', len(code))
        buff += code 
        self.clientsock.send(buff)


if __name__ == '__main__':
    host = str('localhost')
    port = int(1111)
    client = Junk_sendMat(host, port)

    V = np.zeros([3333, 3333])
    for i in range(V.shape[0]):
        for j in range(V.shape[1]):
            V[i,j] = np.random.rand() 
    W = np.zeros([200, 4000])
    for i in range(W.shape[0]):
        for j in range(W.shape[1]):
            W[i,j] = np.random.rand()
    H = np.zeros([4000, 100])
    for i in range(H.shape[0]):
        for j in range(H.shape[1]):
            H[i,j] = np.random.rand()

    client.pushMatrix(V)
    client.pushMatrix(W)
    client.pushMatrix(H)

    code = "self.pushMatrix(self.pop())\n" + "self.pushMatrix(self.pop())";

    client.pushCode(code)
    X = client.popTcp()
    Y = client.popTcp()
    client.clientsock.close()

