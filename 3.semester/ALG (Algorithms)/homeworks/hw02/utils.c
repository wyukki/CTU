#include "utils.h"

int **getMatrix(int *sum, int rows, int columns) {
    int **matrix = malloc(sizeof(int *) * rows);
    if (!matrix) {
        fprintf(stderr, alloc_error, __LINE__);
        return NULL;
    }
    for (int i = 0; i < rows; ++i) {
        matrix[i] = malloc(sizeof(int) * columns);
        if (!matrix[i]) {
            fprintf(stderr, alloc_error, __LINE__);
            free(matrix);
            return NULL;
        }
        for (int j = 0; j < columns; ++j) {
            int num;
            int ret = scanf(j == columns - 1 ? "%d\n" : "%d ", &num);
            if (ret != 1) {
                fprintf(stderr, "Wrong input!\n");
                return NULL;
            }
            matrix[i][j] = num;
            *(sum) += num;
        }
    }
    return matrix;
}

void printMatrix(int **mat, int rows, int columns) {
    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < columns; ++j) {
            printf("%2d", mat[i][j]);
            if (j != columns - 1) {
                printf(" ");
            }
        }
        printf("\n");
    }
}

void printPatterns(int *patterns, int patternTypes) {
    for (int i = 0; i < patternTypes; ++i) {
        int index = i * ELEMENTS_IN_ONE_PATTERN;
        printf("height: %d, width: %d, no. of types: %d\n", patterns[index], patterns[index + 1], patterns[index + 2]);
    }
}
