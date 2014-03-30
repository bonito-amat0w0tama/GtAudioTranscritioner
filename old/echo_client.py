#!/usr/bin/env python
# -*- coding: utf-8 -*-
import socket
import sys
import struct
import numpy as np

# host = str(sys.argv[1])
# port = int(sys.argv[2])

host = "localhost"
port = 1111

byteSizeInt = 4
byteSizeHeader = 4

clientsock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
clientsock.connect((host,port))

# FIXME: name of clientsock
def readHeader(clientsock):
    # bufferSize = 4
    header = clientsock.recv(byteSizeHeader)
    recvSum += byteSizeHeader
    return str(header)

def readInt(clientsock):
    # BUFFERSIZE = 4
    #print "buff"
    buff = clientsock.recv(byteSizeInt)
    recvSum += byteSizeInt
    if buff == '':
        intBuffer = 0
    else:
        # FIXME:variable name 
        intBuffer = struct.unpack('i', buff)[0]
    return intBuffer

def getMatrix(rows, cols, size, clientsock):
    # rows, cols の8バイト分マイナス
    dataSize = size - 8
    matrix = np.zeros((rows, cols))
    for i in range(rows):
        for j in range(cols):
            matrix[i,j] = struct.unpack('f', clientsock.recv(4))[0]
    return  matrix

def pushMatrix(matrix):

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

    clientsock.send(buff)


if __name__ == "__main__":
    V = np.zeros([4000, 100])
    for i in range(V.shape[0]):
        for j in range(V.shape[1]):
            V[i,j] = 1

    pushMatrix(V)

    clientsock.close()

