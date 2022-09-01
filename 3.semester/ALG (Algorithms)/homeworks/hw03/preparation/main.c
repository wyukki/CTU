#include "utils.h"


int main(int argc, char const *argv[])
{
    int ret = EXIT_SUCCESS;
    int allVerts, redVerts;
    if ((scanf("%d %d\n", &allVerts, &redVerts))!= 2) {
        fprintf(stderr, "Error: wrong input!\n");
        return 1;
    }
    graph_t *graph = initGraph(allVerts, redVerts);
    fillGraph(graph);
    printGraph(graph);
    freeGraph(graph);
    return ret;
}


