#include <stdio.h>
#include <stdlib.h>
#include <graph.h>

int main(int argc, char *argv[])
{
    int ret = 0;
    int print = 0;
    char *fname;
    int c = 1;
    fname = argc > 1 ? argv[c] : NULL;
    fprintf(stderr, "Load file ’%s’\n", fname);
    graph_t *graph = allocate_graph();
    int e = load_graph_simple(fname, graph);
    fprintf(stderr, "Load %d edges\n", e);
    if (print)
    {
        print_graph(graph);
    }
    free_graph(&graph);
    return ret;
}