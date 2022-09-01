import socket
import os
import math
import time

# Author: Volodymyr Semenyug
# Sender's code
# First step in sem. work: send simple txt file


BUFFERS_LEN = 1024
fname = "30226_1615145826.webp"

size = os.stat(fname).st_size
UDP_IP_ADDRESS = "192.168.30.11"
#Moj - .22
UDP_PORT_NO = 7615
clientSock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
print("Start sending data to addres", UDP_IP_ADDRESS, "Port no.", UDP_PORT_NO)
print(f"FNAME={fname}")
print(f"SIZE={size}b")

#File name
clientSock.sendto(str.encode(fname), (UDP_IP_ADDRESS, UDP_PORT_NO))
#File size
clientSock.sendto(str.encode(str(size)), (UDP_IP_ADDRESS, UDP_PORT_NO))
#Data
lastIteration = math.ceil(size / BUFFERS_LEN)
with open(fname, "rb") as f:
    for i in range(0, lastIteration) :
        data = f.read(1024)
        clientSock.sendto(data, (UDP_IP_ADDRESS, UDP_PORT_NO))
        time.sleep(0.00999)
print("Connection is closed.")
clientSock.close()
