#include "utils.h"

graph_t *initGraph(int size, int redVerts) {
    graph_t *graph = (graph_t *)malloc(sizeof(graph_t));
    if (!graph) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return NULL;
    }
    graph->edges = NULL;
    graph->size = size;
    graph->numberOfEdges = 0;
    graph->numberOfRedVertexies = redVerts;
    return graph;
}

void fillGraph(graph_t *graph) {
    if (!graph) {
        return;
    }
    printf("%lu\n", sizeof(edge_t));
    for (int i = 0; i < graph->size - 1; ++i) {
        edge_t *edge = (edge_t *)malloc(sizeof(edge_t));
        if (!edge) {
            fprintf(stderr, alloc_error, __LINE__, __FILE__);
            return;
        }
        edge->vert1 = (vertex_t *) malloc(sizeof(vertex_t));
        edge->vert2 = (vertex_t *) malloc(sizeof(vertex_t));
        if (!edge->vert1 || !edge->vert2) {
            fprintf(stderr, alloc_error, __LINE__, __FILE__);
            return;
        }
        if ((scanf("%d %d\n", &edge->vert1->number, &edge->vert2->number)) != 2) {
            fprintf(stderr, "Error: wrong input!\n");
            return;
        }
        isVertexRed(edge->vert1, graph->numberOfRedVertexies);
        isVertexRed(edge->vert2, graph->numberOfRedVertexies);
        graph->edges = edge + (graph->numberOfEdges * sizeof(edge_t));
        graph->numberOfEdges++;
    }
}

void isVertexRed(vertex_t *vertex, int numberOfRedVertexies) {
    if (vertex->number <= numberOfRedVertexies) {
        vertex->isRed = true;
    } else {
        vertex->isRed = false;
    }
}

void printGraph(graph_t *graph) {
    if (!graph) {
        return;
    }
    edge_t *edge = graph->edges;
    for (int i = 0; i < graph->numberOfEdges - 1; ++i, edge += sizeof(edge_t)) {
        printf("%d %d\n", edge->vert1->number, edge->vert2->number);
    }
    
}
void freeGraph(graph_t *graph) {
    if (!graph) {
        return;
    }
    for (int i = 0; i < graph->numberOfEdges - 1; ++i) {
        edge_t *edge = graph->edges + (graph->numberOfEdges * sizeof(edge_t));
        free(edge->vert1);
        free(edge->vert2);
        free(edge);
        graph->numberOfEdges--;
    }
    free(graph->edges);
    free(graph);
}
