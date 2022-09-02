CC=clang
CFLAGS=-Wall -g -O2
LDFLAGS=-pthread
EXTRA_CFLAGS?=-fsanitize=address -fno-omit-frame-pointer
DEP=*.h
SOURCE=factory.c linkedList.c products.c utils.c queue.c
TARGET=factory

$(TARGET): $(SOURCE) $(DEP)
	clang $(CFLAGS) $(EXTRA_CFLAGS) $(LDFLAGS) $(SOURCE) -o $(TARGET)
clean:
	rm -rf $(TARGET)
zip:
	zip hw05 $(SOURCE) Makefile $(DEP)