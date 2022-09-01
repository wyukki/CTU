import socket
import os
import math
import time
import pickle
import binascii
# from sys import getsizeof
import hashlib

# Author: Volodymyr Semenyug
# Sender's code
# Third step in sem. work: use algorithm Selecive Repeat to send multiple data packages at once.

BUFFERS_LEN = 1024
FREAD_SIZE = BUFFERS_LEN - 32 - 28 # BUFFERS_LEN(for data) - crc - packet_num
TTL = 15
WINDOW_SIZE = 4
# wd = b""
WINDOW_DATA = [[], [], [], []]
ACK = [[], [], [], []]

clientAddress = ("127.0.0.1", 9976) # Receiver's IP and Port number
clientSock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
clientSock.settimeout(2)
# server_address = ("192.168.30.24", 3800) # Sender's IP and Port number
server_address = ("", 3800) # Sender's IP and Port number

clientSock.bind(server_address)

fname = "volk_morda_khishchnik_vzglyad_104037_3840x2160.jpg"
size = os.stat(fname).st_size
lastIteration = math.ceil(size / FREAD_SIZE)
print(lastIteration)
print("Start sending data to adress", clientAddress[0], "Port no.", clientAddress[1])
print(f"FNAME={fname}")
print(f"SIZE={size}b")


package_num = 0 # can be up to 8
index = 0


#File name
hashValue = hashlib.sha1()
with open(fname, "rb") as f:
    chunk = 0
    for i in range(0, lastIteration + 2) :
        if i == 0:
            crc = binascii.crc32(str.encode(fname)) # CRC for fname
            arr = [fname, bin(package_num)[2:], crc]
            data_string = pickle.dumps(arr)
            clientSock.sendto(data_string, clientAddress)
            try:
                data_recv, addr = clientSock.recvfrom(BUFFERS_LEN)
                data_recv = pickle.loads(data_recv)
                ACK[i] = data_recv[0]
                print(f"Package no.{i} is {bytes.decode(data_recv[0])}")    
                package_num = (package_num + 1) % (WINDOW_SIZE * 2)
            except socket.timeout:
                print("Timeout")
        elif i == 1:
            crc = binascii.crc32(str.encode(str(size))) # CRC for size
            arr[0] = size
            arr[1] = bin(package_num)[2:]
            arr[2] = crc
            data_string = pickle.dumps(arr)
            clientSock.sendto(data_string, clientAddress)
            try:
                data_recv, addr = clientSock.recvfrom(BUFFERS_LEN)
                data_recv = pickle.loads(data_recv)
                ACK[i] = data_recv[0]
                print(f"Package no.{i} is {bytes.decode(data_recv[0])}")    
                package_num = (package_num + 1) % (WINDOW_SIZE * 2)
            except socket.timeout:
                print("Timeout")
        elif i == 2 or i == 3:
            data = f.read(FREAD_SIZE)
            crc = binascii.crc32(data)
            WINDOW_DATA[i % 4] = data
            arr[0] = data
            arr[1] = bin(package_num)[2:]
            arr[2] = crc
            data_string = pickle.dumps(arr)
            clientSock.sendto(data_string, clientAddress)
            try:
                data_recv, addr = clientSock.recvfrom(BUFFERS_LEN)
                data_recv = pickle.loads(data_recv)
                print(f"Package no.{i} is {bytes.decode(data_recv[0])}")    
                ACK[i] = data_recv[0]
                package_num = (package_num + 1) % (WINDOW_SIZE * 2)
            except socket.timeout:
                print("Timeout")
        else:
            if (ACK[index % WINDOW_SIZE] != b"OK"):
                print(f"Package no.{i-4} is NOT OK")
                while True:
                        crc = binascii.crc32(WINDOW_DATA[index % 4])
                        arr[0] = WINDOW_DATA[index % 4]
                        if (package_num - WINDOW_SIZE < 0):
                            arr[1] = bin((package_num - WINDOW_SIZE) % 8)[2:]
                        else:
                            arr[1] = bin((package_num - WINDOW_SIZE) % 4)[2:]
                        arr[2] = crc
                        data_string = pickle.dumps(arr)
                        clientSock.sendto(data_string, clientAddress)
                        try:
                            data_recv, addr = clientSock.recvfrom(BUFFERS_LEN)
                            data_recv = pickle.loads(data_recv)
                            if (data_recv[0] == b'OK'):
                                print(f"Package no.{i-4} is now OK")
                                ACK[index % WINDOW_SIZE] = b"OK"
                                # package_num = (package_num + 1) % (WINDOW_SIZE * 2)
                                break
                            else:
                                print("NOT OK")
                                TTL -= 1
                                if TTL == 0:
                                    print("Package is in infinite loop")
                                    break
                                clientSock.sendto(data_string, clientAddress) # send one more time
                        except socket.timeout:
                            print("Timeout")
                            if TTL == 0:
                                print("Infinite loop")
                                exit()
                            clientSock.sendto(data_string, clientAddress) # send one more time
                            TTL -= 1
            index += 1
            data = f.read(FREAD_SIZE)
            crc = binascii.crc32(data)
            WINDOW_DATA[i % WINDOW_SIZE] = data
            if (i % 9 == 0):
                crc += 150
            arr[0] = data
            arr[1] = bin(package_num)[2:]
            arr[2] = crc
            data_string = pickle.dumps(arr)
            clientSock.sendto(data_string, clientAddress)
            try:
                data_recv, addr = clientSock.recvfrom(BUFFERS_LEN)
                data_recv = pickle.loads(data_recv)
                print(f"Package no.{i} is {bytes.decode(data_recv[0])}")
                ACK[i % WINDOW_SIZE]=data_recv[0]
                package_num = (package_num + 1) % (WINDOW_SIZE * 2)
            except socket.timeout:
                print("Timeout")

for i in range (0, 4):
    if (ACK[i] == b"NOT OK" or ACK[i] == None):
        while True:
            data = WINDOW_DATA[i]
            crc = binascii.crc32(data)
            arr[0] = data
            arr[1] = bin(package_num)[2:]
            arr[2] = crc
            data_string = pickle.dumps(arr)
            clientSock.sendto(data_string, clientAddress)
            try:
                data_recv, addr = clientSock.recvfrom(BUFFERS_LEN)
                data_recv = pickle.loads(data_recv)
                if (data_recv[0] == b'OK'):
                    print(f"Package no.{lastIteration + 2 - 4} is now OK")
                    i -= 1
                    ACK[index % WINDOW_SIZE] = b"OK"
                    index += 1
                    package_num = (package_num + 1) % 8
                    break
                else:
                    print("NOT OK")
                    TTL -= 1
                    if TTL == 0:
                        print("Package is in infinite loop")
                        break
                    clientSock.sendto(data_string, clientAddress) # send one more time
            except socket.timeout:
                print("Timeout")
                if TTL == 0:
                    print("Infinite loop")
                    exit()
                clientSock.sendto(data_string, clientAddress) # send one more time
                TTL -= 1

def hash_file(fname):
    h = hashlib.sha1()
    chunk = 0
    with open(fname, "rb")as file:
        while chunk != b'':
            chunk = file.read(1024)
            h.update(chunk)
    return h.digest()
hashCode = hash_file(fname)
crc = binascii.crc32(hashCode) # CRC for sha
arr = [hashCode, bin(package_num)[2:], crc]
data_string = pickle.dumps(arr)
clientSock.sendto(data_string, clientAddress)
while True:
    try:
        data1, addr = clientSock.recvfrom(BUFFERS_LEN)
        data1 = pickle.loads(data1)
        if (data1[0] == b'OK'):
            print("SHA is OK")
            package_num = (package_num + 1) % 2
            break
        else:
            print("NOT OK on LINE 208")
            TTL -= 1
            if TTL == 0:
                print("Infinite loop")
                break
            clientSock.sendto(data_string, clientAddress) # send one more time

    except socket.timeout:
        print("Timeout")
        TTL -= 1
        if TTL == 0:
            print("Infinite loop")
            break
        clientSock.sendto(data_string, clientAddress) # send one more time

print("Connection is closed.")

clientSock.close()
