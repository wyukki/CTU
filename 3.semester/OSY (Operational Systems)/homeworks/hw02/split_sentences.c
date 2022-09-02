#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void printSentence(char *text, int sentence_start, int sentence_end);
int main(int argc, char *argv[]) {
    int ret = EXIT_SUCCESS;
    char *text = argc > 1 ? argv[1] : NULL;
    if (!text) {
        fprintf(stderr, "Error: no input text\n");
        return -1;
    }
    int length = strlen(text);
    int sentence_start = 0;
    for (int i = 0; i < length; ++i) {
        if (text[i] == '.' || text[i] == '?' || text[i] == '!') {
            if (sentence_start == 0) {
                printSentence(text, sentence_start, i + 1);
            } else {
                printSentence(text, sentence_start + 1, i);
			}
            sentence_start = i + 1;
        }
    }
    return ret;
}

void printSentence(char *text, int sentence_start, int sentence_end) {
    for (int i = sentence_start; i < sentence_end; ++i) {
        // if (text[i] == '\n') {
        //     text[i] = ' ';
		// 	printf("text[i+1] = %c\n", text[i+1]);
        // } else if (text[i] == ' ' && i == sentence_end - 1) {
		// 	printf("CLEN %d\n", i);
		// 	text[i] = '\0';
        // }
		char c = text[i] != '\n' ? text[i] : ' ';
        printf("%c", c);
    }
    printf("\n");
}
