CC=clang
CFLAGS=-Wall -g -O2
EXTRA_CFLAGS?=-fsanitize=address -fno-omit-frame-pointer
SOURCE=prod-cons.c utils.c
DEP=*.h
TARGET=prod-cons
LDFLAGS=-pthread

$(TARGET) : $(SOURCE) $(DEP)
	$(CC) $(CFLAGS) $(LDFLAGS) $(EXTRA_CFLAGS) $(SOURCE) -o $(TARGET)
clean:
	rm -rf $(TARGET)
