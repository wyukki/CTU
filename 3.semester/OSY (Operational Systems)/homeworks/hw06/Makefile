CC=gcc
CFLAGS=-nostdlib -nostdinc -static -m32 -Wall -g -O2
SOURCE=hexconv.c
TARGET=hexconv

$(TARGET): $(SOURCE)
	$(CC) $(CFLAGS) $(SOURCE) -o $(TARGET)
clean:
	rm -rf $(TARGET)

