CC = clang
CFLAGS = -pedantic -Wall -Werror -std=c99 -g -O3
SOURCE = hw07.c utils.c
DEP = *.h
TARGET = hw07
$(TARGET) : $(SOURCE) $(DEP)
	$(CC) $(CFLAGS) $(SOURCE) -o $(TARGET)
clean:
	rm -rf $(TARGET)
zip:
	zip $(TARGET) $(SOURCE) $(DEP)
