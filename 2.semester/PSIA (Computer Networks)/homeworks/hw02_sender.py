import socket
import os
import math
import pickle
import binascii
import hashlib

# Author: Volodymyr Semenyug
# Sender's code
# Second step in sem. work: CRC added to each message, to assure, that message will be recieved

BUFFERS_LEN = 1024
FREAD_SIZE = BUFFERS_LEN - 32 - 24 # BUFFERS_LEN(for data) - crc - packet_num
TTL = 15

UDP_IP_ADDRESS = "127.0.0.1" # NetDerp localHost
UDP_PORT_NO = 9976 # Target port
clientSock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
clientSock.settimeout(0.5)
# server_address = ("127.0.0.1", 9980) # Reicivng addres
server_address = ("", 3800) # My IP and Port

clientSock.bind(server_address)

fname = "17807338_804159293091827_433660491521314911_o.jpg"
size = os.stat(fname).st_size
package_num = 0
crc = binascii.crc32(str.encode(fname)) # CRC for fname
arr = [fname, bin(package_num)[2:], crc]
data_string = pickle.dumps(arr)

print("Start sending data to adress", UDP_IP_ADDRESS, "Port no.", UDP_PORT_NO)
print(f"FNAME={fname}")
print(f"SIZE={size}b")

#File name
clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO))
while True:
    try:
        data1, addr = clientSock.recvfrom(BUFFERS_LEN)
        data1 = pickle.loads(data1)
        if (data1[0] == b'OK'):
            print("OK")
            package_num = (package_num + 1) % 2
            break
        else:

            print("NOT OK")
            TTL -= 1
            if TTL == 0:
                print("Packege is in infinite loop")
                break
            clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO)) # send one more time

    except socket.timeout:
        print("Timeout")
        if TTL == 0:
            print("Infinite loop")
            exit()
        clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO)) # send one more time
        TTL -= 1

TTL = 15
#File size
while True:
    crc = binascii.crc32(str.encode(str(size))) # CRC for size
    arr[0] = size
    arr[1] = bin(package_num)[2:]
    arr[2] = crc
    data_string = pickle.dumps(arr)
    clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO))
    try:
        data1, addr = clientSock.recvfrom(BUFFERS_LEN)
        data1 = pickle.loads(data1)
        if (data1[0] == b'OK'):
            print("OK")
            package_num = (package_num + 1) % 2
            break
        else:
            print("NOT OK")
            TTL -= 1
            if TTL == 0:
                print("Infinite loop")
                break
            clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO)) # send one more time
    except socket.timeout:
        print("Timeout")
        if TTL == 0:
            print("Infinite loop")
            break
        clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO)) # send one more time
        TTL -= 1

TTL = 15
#Data
lastIteration = math.ceil(size / 968)
hashValue = hashlib.sha1()
with open(fname, "rb") as f:
    chunk = 0
    for i in range(0, lastIteration) :
        data = f.read(968)
        chunk = data
        hashValue.update(chunk)
        crc = binascii.crc32(data) # CRC for data
        # if i != 0 and i % 10 == 0 :
        #     crc += 150
        arr[0] = data
        arr[1] = bin(package_num)[2:]
        arr[2] = crc
        data_string = pickle.dumps(arr)
        arr2 = pickle.loads(data_string)
        clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO))
        while True:
            #time.sleep(0.0099)
            try:
                data1, addr = clientSock.recvfrom(BUFFERS_LEN)
                data1 = pickle.loads(data1)
                if (data1[0] == b'OK'):
                    print(f"Package no.{i} is OK")
                    package_num = (package_num + 1) % 2
                    break
                else:
                    print(f"Package no.{i} is NOT OK")
                    # if i != 0 and i % 10 == 0:
                    #     crc -= 150
                    arr[0] = data
                    arr[1] = bin(package_num)[2:]
                    arr[2] = crc
                    data_string = pickle.dumps(arr)
                    clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO)) # send one more time
            except socket.timeout:
                print("Timeout")
                if TTL == 0:
                    print("Infinite loop")
                    break
                clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO)) # send one more time
                TTL -= 1

hashCode = hashValue.digest()
#SHA
crc = binascii.crc32(hashCode) # CRC for sha
arr = [hashCode, bin(package_num)[2:], crc]
data_string = pickle.dumps(arr)
clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO))
while True:
    try:
        data1, addr = clientSock.recvfrom(BUFFERS_LEN)
        data1 = pickle.loads(data1)
        if (data1[0] == b'OK'):
            print("SHA is OK")
            package_num = (package_num + 1) % 2
            break
        else:
            print("NOT OK")
            TTL -= 1
            if TTL == 0:
                print("Infinite loop")
                break
            clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO)) # send one more time

    except socket.timeout:
        print("Timeout")
        TTL -= 1
        if TTL == 0:
            print("Infinite loop")
            break
        clientSock.sendto(data_string, (UDP_IP_ADDRESS, UDP_PORT_NO)) # send one more time

print("Connection is closed.")

clientSock.close()
