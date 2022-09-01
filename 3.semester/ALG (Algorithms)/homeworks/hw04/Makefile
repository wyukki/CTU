CC=clang
CFLAGS=-pedantic -Wall -Werror -std=c99 -O3 -g
DEP=*.h
SOURCE=main.c utils.c graph.c queue.c
TARGET=hw04

$(TARGET) : $(SOURCE) $(DEP)
	$(CC) $(CFLAGS) $(SOURCE) -o $(TARGET)
clean:
	rm -rf $(TARGET)