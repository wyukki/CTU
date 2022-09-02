CC=clang
CFLAGS= -Wall
EXTRA_CFLAGS= 
DEP=*.h
TARGET=forkpipe

all: $(TARGET) nsd

%.o: %.c
	$(CC) -c -o $@ $< $(CFLAGS) $(EXTRA_CFLAGS)

$(TARGET): forkpipe.o
	$(CC) -o $@ $^

nsd: nsd.o nd.o nsd_main.o $(DEP)
	$(CC) $(CFLAGS) $(EXTRA_CFLAGS) nsd_main.o nsd.o nd.o -o $@

clean: 
	rm forkpipe nsd *.o
