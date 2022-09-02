TARGET=preparation
SOURCE=arguments.c
$(TARGET) : $(SOURCE)
	clang -pthread -pedantic -Wall -Werror -std=c99 -g $(SOURCE) -o $(TARGET)
clean:
	rm -rf $(TARGET)