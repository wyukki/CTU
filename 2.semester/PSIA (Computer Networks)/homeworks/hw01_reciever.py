import socket
import sys
import math
import time

# Author: Roman Danilchenko
# Reciever's code
# First step in sem. work: recieve simple txt file

BUFFERS_LEN = 1024
# Create a UDP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_address = ("192.168.30.11", 7615)
sock.bind(server_address)

print("Waiting for packages...")

#fname
data1, addr = sock.recvfrom(BUFFERS_LEN)
fname = data1
print("FILE NAME: ", fname.decode())

#size
data2, addr = sock.recvfrom(BUFFERS_LEN)
size = int(data2.decode())
print("FILE SIZE:", size)

#compute the number of the last iteration
last_iter = math.ceil(size / BUFFERS_LEN)

with open(fname, "wb") as f:
    for i in range(0,last_iter):
        data3, addr = sock.recvfrom(BUFFERS_LEN)
        # time.sleep(0.007)
        f.write(data3)
        time.sleep(0.0099)

print("Connection is closed!")
sock.close()
