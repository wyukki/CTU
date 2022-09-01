import socket
import sys
import math
import time
import pickle
import binascii
import hashlib

# Author: Roman Danilchenko
# Reciever's code
# Third step in sem. work: use algorithm Selecive Repeat to send multiple data packages at once.

BUFFERS_LEN = 1024  
DATA_TTL = 15

client_adress = ("192.168.30.27", 3800)

# Create a UDP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_address = ("192.168.30.15", 7610)

#set timeout
sock.settimeout(2)
sock.bind(server_address)

print("Waiting for packages...")

MAIN_ARRAY = [[],[],[],[]]
ACK_ARRAY = [[],[],[],[]]

#fname
pack_num = 0
try:
    arr, addr = sock.recvfrom(BUFFERS_LEN)
    arr = pickle.loads(arr)

    MAIN_ARRAY[0] = arr

    fname = arr[0]
    packet_number = arr[1]
    crc_code = arr[2]
    crc = binascii.crc32(str.encode(fname))

    arr_reply = []
    if crc_code == crc :
        arr_reply = [b"OK", packet_number]
        data_string = pickle.dumps(arr_reply)
        print("Package number - 0 is OK")
        sock.sendto(data_string, client_adress)
        pack_num = (pack_num + 1) % 8
        ACK_ARRAY[0] = "OK"
        # break
    else:
        arr_reply = [b"NOT OK", packet_number]
        data_string = pickle.dumps(arr_reply)
        print("Package number - 0 is NOT OK")
        sock.sendto(data_string, client_adress)
        ACK_ARRAY[0] = "NOT OK"
except socket.timeout:
    print("TIMEOUT")
    DATA_TTL -= 1
    if DATA_TTL == 0:
        print("LEAVING.")
        sock.close()
        exit()

print("FILE NAME:", fname)

DATA_TTL = 15

#size
try:
    arr, addr = sock.recvfrom(BUFFERS_LEN)
    arr = pickle.loads(arr)

    MAIN_ARRAY[1] = arr

    size = arr[0]
    packet_number = arr[1]
    crc_code = arr[2]
    
    if int(packet_number,base=2) == (pack_num - 1) % 8:
        sock.sendto(b"OK", client_adress)
        
    crc = binascii.crc32(str.encode(str(size)))
    if crc_code == crc :
        arr_reply = [b"OK", packet_number]
        data_string = pickle.dumps(arr_reply)
        print("Package number - 1 is OK")
        sock.sendto(data_string, client_adress)
        pack_num = (pack_num + 1) % 8
        ACK_ARRAY[1] = "OK"
    else:
        arr_reply = [b"NOT OK", packet_number]
        data_string = pickle.dumps(arr_reply)
        print("Package number - 1 is NOT OK")
        sock.sendto(data_string, client_adress)
        ACK_ARRAY[1] = "NOT OK"
except socket.timeout:
    print("TIMEOUT")
    DATA_TTL -= 1
    if DATA_TTL == 0:
        print("LEAVING.")
        sock.close()
        exit()

print("FILE SIZE:", size)

#compute the number of the last iteration
last_iter = math.ceil(size / 964) 
print("last_iter: ", last_iter)

DATA_TTL = 15

counter = 2

data_storage = [[], [], [], []] #write received data here

ret = True
counter_for_data = 0
counter_for_saved = 0
with open(fname, "wb") as f:
    for i in range(0,last_iter):   
        try:    
            arr, addr = sock.recvfrom(BUFFERS_LEN)
            arr = pickle.loads(arr)
        
            MAIN_ARRAY[counter] = arr                

            main_data = arr[0]
            packet_number = arr[1]
            crc_code = arr[2]
            crc = binascii.crc32(main_data)
            if i > 2:
                if int(packet_number,base=2) == (pack_num - 4) % 8:
                    while True:
                        crc = binascii.crc32(main_data)
                        if crc_code == crc :
                            arr_reply = [b"OK", packet_number]
                            data_storage[(i-4) % 4] = main_data 
                            data_string = pickle.dumps(arr_reply)
                            sock.sendto(data_string, client_adress)
                            print(f"Package number - {i - 2} is now OK")
                            ACK_ARRAY[counter] = "OK"
                            DATA_TTL = 15
                            
                            arr, addr = sock.recvfrom(BUFFERS_LEN)
                            arr = pickle.loads(arr)
                        
                            MAIN_ARRAY[counter] = arr                

                            main_data = arr[0]
                            packet_number = arr[1]
                            crc_code = arr[2]
                            crc = binascii.crc32(main_data)
                            break              
                        else:
                            arr_reply = [b"NOT OK", packet_number]
                            data_string = pickle.dumps(arr_reply)
                            sock.sendto(data_string, client_adress)
                            print(f"Package number - {i - 2} is NOT OK")
                            ACK_ARRAY[counter] = "NOT OK"      

            if i > 3:
                if ret == False and ACK_ARRAY[counter_for_data] == "NOT OK":
                    ret = False
                else: 
                    f.write(bytes(data_storage[counter_for_data]))
                    counter_for_saved += 1
                    counter_for_data = (counter_for_data + 1) % 4
                    ret = True

            if crc_code == crc :
                arr_reply = [b"OK", packet_number]
                data_storage[i % 4] = main_data
                data_string = pickle.dumps(arr_reply)
                sock.sendto(data_string, client_adress)
                pack_num = (pack_num + 1) % 8
                print(f"Package number - {i} is OK")
                ACK_ARRAY[counter] = "OK"
                DATA_TTL = 15              
            else:
                arr_reply = [b"NOT OK", packet_number]
                data_string = pickle.dumps(arr_reply)
                pack_num = (pack_num + 1) % 8
                sock.sendto(data_string, client_adress)
                print(f"Package number - {i} is NOT OK")
                ACK_ARRAY[counter] = "NOT OK"
        
            counter  = (counter + 1) % 4

        except socket.timeout:
            print("TIMEOUT")
            DATA_TTL -= 1
            if DATA_TTL == 0:
                print("LEAVING.")
                sock.close()
                exit()

    if last_iter - counter_for_saved > 0:
        print(last_iter)
        print(counter_for_saved)
        for i in range(0, last_iter - counter_for_saved):
            f.write(data_storage[counter_for_data])
            counter_for_data = (counter_for_data + 1) % 4

# #hash
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
# print("HASH: ", hash_code)

while True:
    try:
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
    except socket.timeout:
        print("TIMEOUT")
        DATA_TTL -= 1
        if DATA_TTL == 0:
            print("LEAVING.")
            sock.close()
            exit()
# # sock.sendto(b"Everything gone right", client_adress)

print("Got all packages!")
print("Connection is closed!")
sock.close()
