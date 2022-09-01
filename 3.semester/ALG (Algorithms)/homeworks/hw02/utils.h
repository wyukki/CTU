#ifndef UTILS_H
#define UTILS_H
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#define ELEMENTS_IN_ONE_PATTERN 3

static const char * const alloc_error = "Allocation error on line% d!\n";

/**
 * Reads input from stdin and creates a matrix
 */
int **getMatrix(int *sum, int rows, int columns);
/**
 * Prints matrix
 */
void printMatrix(int **mat, int rows, int columns);
/**
 * Prints all types of patterns
 */
void printPatterns(int *patterns, int patternTypes);


#endif
