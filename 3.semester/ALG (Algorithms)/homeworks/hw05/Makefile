CC = clang
CFLAGS = -pedantic -Wall -Werror -std=c99 -Wall -Werror -g
SOURCE = hw05.c utils.c tree.c
DEP = *.h
TARGET = hw05

$(TARGET) : $(SOURCE) $(DEP)
	$(CC) $(CFLAGS) $(SOURCE) -o $(TARGET)
clean:
	rm -rf $(TARGET)
zip:
	zip $(TARGET) Makefile $(SOURCE) $(DEP)
