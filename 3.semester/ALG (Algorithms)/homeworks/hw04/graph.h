#ifndef _GRAPH_H
#define _GRAPH_H
#include "utils.h"

typedef struct {
    int number;   // village number
    bool isFriendly;    // true if village number is <= number of freindly villages
    bool isVisited;     // true if this has village been already viseted in BFS
    int saturation;     // current saturation in village 
    int food;           // number of packages needed to come to this village
} vertex_t;

typedef struct {
    vertex_t **vertices;    // array of vertices
    int size;   // number of vertices
} graph_t;
/**
 * Creates and return graph object
 */
graph_t *initGraph(int villages);
/**
 * Adds to graph every vertex from adjacency list
 */
void fillGraph(graph_t *graph, int villages, int friendlyVillages, int maxSaturation);
/**
 * Prints the content of graph
 */
void printGraph(graph_t *graph, int villages);
/**
 * Returns true if village number <= number of freindly villages
 */
bool isFreindly(int vertexNumber, int friendlyVillages);
/**
 * Frees graph object
 */
void freeGraph(graph_t *graph, int villages);

#endif
