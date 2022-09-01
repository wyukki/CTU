#ifndef _TREE_H
#define _TREE_H
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <math.h>

typedef struct node {
    struct node *left;
    struct node *right;
    int number;
    bool isRed;
    int counterOfRedBefore;
} node_t;

typedef struct {
    int node1;
    int node2;
    int difference;
} result_t;

node_t *initTree(int allVertices);
node_t *addToTree(node_t *node, int **adjacencyMatrix, int numberOfRedVertices,
                  int parent, int startIndex);
void removeFromAdjacencyMatrix(int **adjacencyMatrix, int parent, int child);

bool isRed(int numberOfVertex, int numberOfRedVertices);

bool vertexHasAChild(int **adjacencyMatrix, int vertex);

int getFirstChild(int **adjacencyMatrix, int vertex);

void printPostOrder(node_t *node);

void findResult(node_t *node, node_t *parent, int numberOfRedVertices);

void printResult();

#endif
