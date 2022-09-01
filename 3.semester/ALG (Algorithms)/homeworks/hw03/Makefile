CC = clang
CFLAGS = -pedantic -Wall -Werror -std=c99 -g -lm
SOURCE = main.c tree.c
TARGET = hw03
DEP = *.h

$(TARGET) : $(SOURCE) $(DEP)
	$(CC) $(CFLAGS) $(SOURCE) -o $(TARGET)

clean:
	rm -rf $(TARGET)