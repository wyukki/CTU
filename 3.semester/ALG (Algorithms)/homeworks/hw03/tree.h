#ifndef _TREE_H
#define _TREE_H
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <math.h>

typedef struct node {
    struct node *left; // pointer to left child
    struct node *right; // pointer to right child
    int number; // value of node
    bool isRed; // true if node is red (number is <= R), false otherwise
    int counterOfRedBefore; // counter of red parents
} node_t;

typedef struct { // represents result, that should be displayed in the end
    int node1; // value of first node
    int node2; // value of second node
    int difference; // difference in count of red nodes on the left and right
} result_t;

/**
 * Creates and returns pointer to root.
 */
node_t *initTree(int allVertices);
/**
 * Adds to tree new node w.r.t. adjacency matrxi
 */
node_t *addToTree(node_t *node, int **adjacencyMatrix, int numberOfRedVertices,
                  int parent, int startIndex);
/**
 * Removes from adjacency matrix used node
 */
void removeFromAdjacencyMatrix(int **adjacencyMatrix, int parent, int child);

/**
 * Returns true if numberOfVertex <= numberOfRedVertices, false otherwise
 */
bool isRed(int numberOfVertex, int numberOfRedVertices);

/**
 * Returns true, if vertex still has a at least one child, false otherwise
 */
bool vertexHasAChild(int **adjacencyMatrix, int vertex);

/**
 * Returns value of first vertex child
 */
int getFirstChild(int **adjacencyMatrix, int vertex);

/**
 * Prints tree in postorder traverse
 */
void printPostOrder(node_t *node);

/**
 * Main function, traverses tree in post order and counts difference in red nodes.
 */
void findResult(node_t *node, node_t *parent, int numberOfRedVertices);

/**
 * Prints result to stdout.
 */
void printResult();

#endif
