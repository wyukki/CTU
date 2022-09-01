CC=clang
CFLAGS=-pedantic -Wall -Werror -std=c99 -g -O3
TARGET=hw02
# SOURCE = workingShit.c utils.c
# SOURCE=try8.c utils.c
# SOURCE=main.c utils.c
# SOURCE=try_7_without_prints.c utils.c
SOURCE = hw02.c utils.c
DEP = *.h
$(TARGET): $(SOURCE) $(DEP)
	$(CC) $(CFLAGS) $(SOURCE) -o $@
clean:
	rm $(TARGET) *.o
