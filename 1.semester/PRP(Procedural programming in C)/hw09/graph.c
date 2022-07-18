#include "graph.h"

#include <stdio.h>
#include <stdlib.h>
// #include <assert.h>

const char *const alloc_error = "Allocation error!\n";

/* Allocate a new graph and return a reference to it. */
graph_t *allocate_graph() {
    graph_t *graph = malloc(sizeof(graph_t));
    if (!graph) {
        frpintf(stderr, alloc_error);
        free(graph);
        return NULL;
    }
    graph->capacity = 0;
    graph->num_of_edges = 0;
    graph->edges = NULL;
    return graph;
}
/* Free all allocated memory and set reference to the graph to NULL. */
void free_graph(graph_t **graph) {
    if (!graph || !*graph) {
        return;
    }
    if ((*graph)->capacity > 0) {
        free((*graph)->edges);
    }
    free(*graph);
    *graph = NULL;
}

/* Load a graph from the text file. */
void load_txt(const char *fname, graph_t *graph) {
    FILE *file = fopen(fname, "r");
    int counter = 0;
    int exit = 0;
    while (file && !exit) {
        edge_t *edge = graph->edges + graph->num_of_edges;
        while (graph->num_of_edges < graph->capacity) {
            int r = fscanf(file, "%d %d %d\n", &(edge->from), &(edge->to), &(edge->cost));
            if (r == 3) {
                graph->num_of_edges++;
                counter++;
                edge++;
            } else {
                exit = 1;
                break;
            }
        }
    }
    if (file) {
        fclose(file);
    }
}
/* Load a graph from the binary file. */
void load_bin(const char *fname, graph_t *graph) { return; }

/* Save the graph to the text file. */
void save_txt(const graph_t *const graph, const char *fname) {
    FILE *file = fopen(fname, "a");
    int num_egde = graph->num_of_edges;
    edge_t *edge;
    while (num_egde) {
        fprintf(file, edge->from, edge->to, edge->cost);
        num_egde--;
    }
    fclose(file);
}
/* Save the graph to the binary file. */
void save_bin(const graph_t *const graph, const char *fname) { return; }
