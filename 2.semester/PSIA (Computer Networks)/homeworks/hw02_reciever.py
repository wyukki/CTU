import socket
import sys
import math
import time
import pickle
import binascii
import hashlib

# Author: Roman Danilchenko
# Reciever's code
# Second step in sem. work: CRC added to each message, to assure, that message will be recieved

BUFFERS_LEN = 1024
FREAD_SIZE = BUFFERS_LEN - 10 - 1

DATA_TTL = 15

# client_adress = ("192.168.30.24", 3800)
# client_adress = ("127.0.0.1", 14001)
client_adress = ("192.168.30.24", 9980)

# Create a UDP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_address = ("192.168.30.22", 7610)

#set timeout
sock.settimeout(2)
sock.bind(server_address)

print("Waiting for packages...")

#fname
pack_num = 0
while True:
    try:
        arr, addr = sock.recvfrom(BUFFERS_LEN)
        arr = pickle.loads(arr)

        fname = arr[0]
        packet_number = arr[1]
        crc_code = arr[2]
        crc = binascii.crc32(str.encode(fname))

        arr_reply = []
        if crc_code == crc :
            arr_reply = [b"OK", packet_number]
            data_string = pickle.dumps(arr_reply)
            sock.sendto(data_string, client_adress)
            pack_num = (pack_num + 1) % 2
            break
        else:
            arr_reply = [b"NOT OK", packet_number]
            data_string = pickle.dumps(arr_reply)
            sock.sendto(data_string, client_adress)
    except socket.timeout:
        print("TIMEOUT")
        DATA_TTL -= 1
        if DATA_TTL == 0:
            print("LEAVING.")
            sock.close()
            exit()

print("PACKET NUMBER:", packet_number)
print("FILE NAME:", fname)

DATA_TTL = 15

#size
while True:
    try:
        arr, addr = sock.recvfrom(BUFFERS_LEN)
        arr = pickle.loads(arr)

        size = arr[0]
        packet_number = arr[1]
        crc_code = arr[2]
        
        if int(packet_number,base=2) == (pack_num - 1) % 2:
            sock.sendto(b"OK", client_adress)
            continue
        
        crc = binascii.crc32(str.encode(str(size)))
        if crc_code == crc :
            arr_reply = [b"OK", packet_number]
            data_string = pickle.dumps(arr_reply)
            sock.sendto(data_string, client_adress)
            pack_num = (pack_num + 1) % 2
            break
        else:
            arr_reply = [b"NOT OK", packet_number]
            data_string = pickle.dumps(arr_reply)
            sock.sendto(data_string, client_adress)
    except socket.timeout:
        print("TIMEOUT")
        DATA_TTL -= 1
        if DATA_TTL == 0:
            print("LEAVING.")
            sock.close()
            exit()


print("FILE SIZE:", size)

#compute the number of the last iteration
last_iter = math.ceil(size / 968)

DATA_TTL = 15

with open(fname, "wb") as f:
    for i in range(0,last_iter):
        while True:    
            try:    
                arr, addr = sock.recvfrom(BUFFERS_LEN)
                arr = pickle.loads(arr)
            
                main_data = arr[0]
                packet_number = arr[1]
                crc_code = arr[2]

                if int(packet_number,base=2) == (pack_num - 1) % 2:
                    sock.sendto(b"OK", client_adress)
                    continue
                    
                crc = binascii.crc32(main_data)
                
                if crc_code == crc :
                    arr_reply = [b"OK", packet_number]
                    data_string = pickle.dumps(arr_reply)
                    sock.sendto(data_string, client_adress)
                    pack_num = (pack_num + 1) % 2
                    print(f"Package number - {i} is OK")
                    f.write(main_data)
                    # time.sleep(0.0099)
                    break
                else:
                    arr_reply = [b"NOT OK", packet_number]
                    data_string = pickle.dumps(arr_reply)
                    sock.sendto(data_string, client_adress)
                    print(f"Package number - {i} is NOT OK")
            
            except socket.timeout:
                print("TIMEOUT")
                DATA_TTL -= 1
                if DATA_TTL == 0:
                    print("LEAVING.")
                    sock.close()
                    exit()

#hash
def hash_file(fname):
   """"This function returns the SHA-1 hash
   of the file passed into it"""
   # make a hash object
   h = hashlib.sha1()

   # open file for reading in binary mode
   with open(fname,'rb') as file:

       # loop till the end of the file
       chunk = 0
       while chunk != b'':
           # read only 1024 bytes at a time
           chunk = file.read(1024)
           h.update(chunk)

   # return the representation of digest
   return h.digest()

hash_code = hash_file(fname)

while True:
    arr, addr = sock.recvfrom(BUFFERS_LEN)
    arr = pickle.loads(arr)

    hash_c = arr[0]
    packet_number = arr[1]
    crc_code = arr[2]
    
    crc = binascii.crc32(hash_c)

    arr_reply = []
    if crc_code == crc and hash_code == hash_c:
        arr_reply = [b"OK", packet_number]
        data_string = pickle.dumps(arr_reply)
        sock.sendto(data_string, client_adress)
        pack_num = (pack_num + 1) % 2
        print("SHA is OK")
        break
    else:
        arr_reply = [b"NOT OK", packet_number]
        data_string = pickle.dumps(arr_reply)
        sock.sendto(data_string, client_adress)

sock.sendto(b"Everything gone right", client_adress)

print("Got all packages!")
print("Connection is closed!")
sock.close()
