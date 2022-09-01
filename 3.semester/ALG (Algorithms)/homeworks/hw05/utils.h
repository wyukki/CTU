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

int **getOperations(int numberOfOperations);
void printOpearations(int **operations, int numberOfOperations);
void freeOperations(int **opearations, int numberOfOpeartions);
int getOperationType(int **operation, int operationNumber);
int getIntervalLength(int **operations, int operationNumber);


#endif
