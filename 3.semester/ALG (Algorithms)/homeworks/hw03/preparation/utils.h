#ifndef UTILS_H
#define UTILS_H
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

static const char *const alloc_error = "Allocation error on line %d in file %s!\n";

typedef struct vertex {
    int number;
    bool isRed;
} vertex_t;

typedef struct edge {
    vertex_t *vert1;
    vertex_t *vert2;
} edge_t;

typedef struct {
    edge_t *edges;
    int size;
    int numberOfRedVertexies;
    int numberOfEdges;
} graph_t;

graph_t *initGraph(int size, int redVerts);
void fillGraph(graph_t *graph);
void isVertexRed(vertex_t *vertex, int numberOfRedVertexies);
void printGraph(graph_t *graph);
void freeGraph(graph_t *graph);

#endif
