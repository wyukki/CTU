#ifndef _UTILS_H
#define _UTILS_H
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>

#define NUMBER_OF_OPERANDS 3
#define MAX_NUMBER_OF_VERTICES 70000
#define INSERT_OPERATION 0
#define DELETE_OPERATION 1
#define UNDEFINED_OPERATION -1

/**
 * Reads input and creates an array of operations.
 */
int **getOperations(int numberOfOperations);
/**
 * Prints operation to stdout.
 */
void printOpearations(int **operations, int numberOfOperations);
/**
 * Frees memory
 */
void freeOperations(int **opearations, int numberOfOpeartions);
/**
 * Returns INSERT_OPERATION or DELETE_OPERATION
 */
int getOperationType(int **operation, int operationNumber);
/**
 * Returns an interval of certain operation
 */
int getIntervalLength(int **operations, int operationNumber);


#endif
