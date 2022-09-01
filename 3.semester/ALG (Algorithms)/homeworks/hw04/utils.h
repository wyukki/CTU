#ifndef _UTILS_H
#define _UTILS_H
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <time.h>

#define NUMBER_OF_NEIGHBOURS 4

static const char *const alloc_error = "Allocation error on line %d in file %s!\n";
/**
 * Creates and returns adjacency list
 */
int **getAdjacencyList(int villages, int routes);
/**
 * Adds to adjacency list villages
 */
void addToAdjacencyList(int **adjacencyList, int village1, int village2);
/**
 * Prints the content of adjacency list
 */
void printAdjacencyList(int **adjacencyList, int villages);
/**
 * Frees adjacency list
 */
void freeAdjacencyList(int **adjacencyList, int villages);

#endif
