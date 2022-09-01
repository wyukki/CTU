#include "graph.h"

#include "utils.h"

graph_t *initGraph(int villages) {
    graph_t *graph = (graph_t *)malloc(sizeof(graph_t));
    if (!graph) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return NULL;
    }
    graph->size = 0;
    graph->vertices = (vertex_t **)malloc(sizeof(vertex_t *) * villages);
    if (!graph->vertices) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        free(graph);
        return NULL;
    }
    return graph;
}

void fillGraph(graph_t *graph, int villages, int friendlyVillages, int maxSaturation) {
    if (!graph || !graph->vertices) {
        return;
    }
    for (int i = 0; i < villages; ++i) {
        vertex_t *newVertex = (vertex_t *)malloc(sizeof(vertex_t));
        if (!newVertex) {
            fprintf(stderr, alloc_error, __LINE__, __FILE__);
            free(graph->vertices);
            free(graph);
            exit(-1);
        }
        newVertex->number = i + 1;
        newVertex->saturation = isFreindly(i + 1, friendlyVillages) ? maxSaturation : 0;
        newVertex->food = 0;
        newVertex->isFriendly = isFreindly(i + 1, friendlyVillages);
        newVertex->isVisited = false;
        graph->vertices[i] = newVertex;
    }
}

void printGraph(graph_t *graph, int villages) {
    for (int i = 0; i < villages; ++i) {
        vertex_t *entry = graph->vertices[i];
        printf("Vertex %2d is: %2d %2s f:%2d s:%2d\n", i + 1,
               entry->number, entry->isFriendly ? "true " : "false", entry->food, entry->saturation);
    }
}

bool isFreindly(int vertexNumber, int friendlyVillages) {
    return vertexNumber <= friendlyVillages;
}

void freeGraph(graph_t *graph, int villages){
    if (!graph) {
        return;
    }
    for (int i = 0; i < villages; ++i) {
        free(graph->vertices[i]);
    }
    free(graph->vertices);
    free(graph);
}
