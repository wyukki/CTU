#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

const char *const alloc_error = "Error: Allocation error!\n";

enum {
    SIZE = 10,
    OPEN_FILE_ERROR = -1
};

void free_line(char *line);
int length_of_line(char *line);                // computing length of the line
int length_of_pattern(char *pattern);          // computing length of the pattern
char *read_line_from_file(FILE *f);            // reads a line from a file\stdin
bool compare_line(char *line, char *pattern);  // comparing two lines and return true if pattern is matched false otherwise
void print_line(char *line);                   // prints line on stdout

/**
 * HW07 - Searching text in files.
 * Mandatory part: user gives a pattern of wanted text and file name. Program prints lines,
 * where pattern was found, or nothing if pattern wasn't matched.
 * Optional part:
 *      A). Reading from stdin
 *      B). Regex - NOT IMPLEMENTED
 *      C). Colorful text - NOT IMPLEMENTED
 */

int main(int argc, char *argv[]) {
    int ret = EXIT_FAILURE;
    bool check_pattern;
    char *fname = NULL;
    char *pattern = NULL;
    char *line = NULL;
    FILE *f = NULL;
    switch (argc) {  // different number of arguments in optional part
        case 2:      // reading from stdin
            pattern = argv[1];
            f = stdin;
            break;
        case 3:  // reading from a file
            pattern = argv[1];
            fname = argv[2];
            if ((f = fopen(fname, "r")) == NULL) {
                fprintf(stderr, "Error: open file '%s'\n", fname);
                ret = OPEN_FILE_ERROR;
            }
            break;

        case 4:  // making text colorful
            printf("Not impemented yet!\n");
            return EXIT_FAILURE;
            pattern = argv[2];
            break;
    }
    do {
        line = read_line_from_file(f);
        if (!line) {
            break;
        }
        check_pattern = compare_line(line, pattern);
        if (check_pattern) {
            print_line(line);
            ret = EXIT_SUCCESS;
        }
        free_line(line);
    } while (line);
    if (f != stdin && (fclose(f) == EOF)) {
        fprintf(stderr, "Error: close file error!\n");
        ret = OPEN_FILE_ERROR;
    }
    return ret;
}

void free_line(char *line) {
    free(line);
}
int length_of_line(char *line) {
    int len = 0;
    int idx = 0;
    while (1) {
        if (line[idx] == '\n') {
            break;
        }
        len++;
        idx++;
    }
    return len;
}

int length_of_pattern(char *pattern) {
    int len = 0;
    int idx = 0;
    while (1) {
        if (pattern[idx] == '\0') {
            break;
        }
        len++;
        idx++;
    }
    return len;
}

char *read_line_from_file(FILE *f) {
    char c = '\0';
    int size = SIZE;
    char *line = malloc(sizeof(char) * size);

    char *tmp = NULL;
    int i = 0;
    int capacity = 0;
    while (c != '\n') {
        c = fgetc(f);
        if (c == EOF) {
            free(line);
            return NULL;
        }
        if (capacity == size) {
            tmp = realloc(line, size * 2);
            if (!tmp) {
                fprintf(stderr, alloc_error);
                free(line);
                return NULL;
            }
            size *= 2;
            line = tmp;
        }
        capacity += 1;
        line[i] = c;
        i++;
    }
    return line;
}

bool compare_line(char *line, char *pattern) {
    bool ret = false;
    int line_length = length_of_line(line);
    int pattern_length = length_of_pattern(pattern);
    int j = 0;
    int count = 1;

    if (pattern_length == 1) {
        for (int i = 0; i < line_length; ++i) {
            if (pattern[0] == line[i]) {
                ret = true;
                return ret;
            }
        }
    } else {
        for (int i = 0; i < line_length; ++i) {
            if (pattern[j] == line[i]) {
                if (count == pattern_length) {
                    ret = true;
                    return ret;
                }
                count++;
                j++;
            } else if (count > 1 && pattern[j] != line[i]) {
                count = 1;
                j = 0;
            }
        }
    }
    return ret;
}
void print_line(char *line) {
    int i = 0;
    while (line[i] != '\n') {
        printf("%c", line[i]);
        i++;
    }
    printf("\n");
}
