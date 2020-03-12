import socket
import pickle
import json
import imageio
import numpy

hote = "192.168.2.4"
port = 22346

socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

def initConnexion():
	socket.connect((hote,port))
	socket.send(bytes("init",'utf-8'))
	liste_eleves = socket.recv(255)
	return json.loads(liste_eleves)

def sendFile(filepath):
	file = open(filepath,'rb')
	socket.sendfile(file)
	return json.loads(socket.recv(255))

def stopConnexion():
	socket.close()
