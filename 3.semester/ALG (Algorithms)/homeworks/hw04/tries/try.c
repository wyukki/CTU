#include "graph.h"
#include "queue.h"
#include "utils.h"

queue_t *queue;

int unvisitedVillage(int **adjacencyList, int village) {
    for (int i = 0; i < NUMBER_OF_NEIGHBOURS; ++i) {
        if (adjacencyList[village][i] != -1) {
            return adjacencyList[village][i];
        }
    }
    return -1;
}

void traverseAdjacencyList(int **adjacencyList, int village, int prevVillage) {
    if (village == -1) {
        return;
    }
    if (prevVillage != -1) {
        removeFromAdjacencyMatrix(adjacencyList, prevVillage, village);
    }
    if (!vertexHasAChild(adjacencyList, village)) {
        return;
    }
    printf("Visited: %d\n", village);
    prevVillage = village;
    village = unvisitedVillage(adjacencyList, village);
    traverseAdjacencyList(adjacencyList, village, prevVillage);
}

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    clock_t start = clock();
    int villages, routes, freindlyVillages, saturation;
    if ((scanf("%d %d %d %d\n", &villages, &routes, &freindlyVillages, &saturation)) != 4) {
        fprintf(stderr, "Error: wrong input!\n");
        return -1;
    }
    printf("Number of villages=%d, numberOfRoutes=%d\n", villages, routes);
    int **adjacencyList = getAdjacencyList(villages, routes);
    nodes_t *nodes = (nodes_t *)malloc(sizeof(nodes_t));
    nodes->size = 0;
    nodes->vertices = (vertex_t **)malloc(sizeof(vertex_t *) * villages);
    for (int i = 0; i < villages; ++i) {
        nodes->vertices[i] = (vertex_t *)malloc(sizeof(vertex_t));
        nodes->vertices[i]->number = i + 1;
        nodes->vertices[i]->isFriendly = i + 1 <= freindlyVillages;
        nodes->vertices[i]->saturation = i + 1 <= freindlyVillages ? saturation : 0;
        nodes->vertices[i]->food = 0;
    }
    // nodes_t *nodes = (nodes_t *)malloc(sizeof(nodes_t));
    // for (int i = 0; i < villages; ++i) {
    //     vertex_t * newVertex = (vertex_t *)malloc(sizeof(vertex_t));
    //     newVertex->number = i + 1;
    //     newVertex->isFriendly = i <= freindlyVillages;
    //     newVertex->saturation = 0;
    //     newVertex->food = 0;
    //     nodes->vertices = newVertex;
    //     // nodes->vertices->number = i + 1;
    //     // nodes->vertices->isFriendly = i <= freindlyVillages;
    //     // nodes->vertices->saturation = 0;
    //     // nodes->vertices->food = 0;
    // }
    for (int i = 0; i < villages; ++i) {
        vertex_t *entry = nodes->vertices[i];
        printf("Vertex %d is: %d %d %d %d\n", i+1, entry->number, entry->isFriendly, entry->saturation, entry->food);
    }
    queue = initQueue();
    if (!queue) {
        freeAdjacencyList(adjacencyList, villages);
        return 1;
    }
    // printAdjacencyList(adjacencyList, villages);
    // int village = 1;
    // for (int i = 0; i < routes; ++i) {
    //     int prevVillage = village;
    //     village = unvisitedVillage(adjacencyList, village);
    //     // adjacencyList[prevVillage][village]
    // }
    // node_t *graph = initGraph(villages);
    // addToGraph(graph, adjacencyList, freindlyVillages, -1, 1);
    // traverseAdjacencyList(adjacencyList, village, -1);
    freeAdjacencyList(adjacencyList, villages);
    // dfs(graph, arrayOfVertices);
    freeQueue(queue);
    // freeGraph(graph);
    clock_t end = clock();
    printf("TIME: %f\n", (double)((end - start) / CLOCKS_PER_SEC));
    return ret;
}
