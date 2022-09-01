#include "graph.h"
#include "queue.h"
#include "utils.h"

queue_t *queue;
graph_t *graph;
int **adjacencyList;
int minProducts = 0;
int villagesWith1 = 0;
vertex_t *getNeighbour(int parent, int index);
void bfs(int maxSaturation);

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    clock_t start = clock();
    int villages, routes, freindlyVillages, saturation;
    if ((scanf("%d %d %d %d\n", &villages, &routes, &freindlyVillages, &saturation)) != 4) {
        fprintf(stderr, "Error: wrong input!\n");
        return -1;
    }
    printf("Number of villages=%d, numberOfRoutes=%d,\nfriendlyVillages=%d, maxSaturation=%d\n",
             villages, routes, freindlyVillages, saturation);
    adjacencyList = getAdjacencyList(villages, routes);
    // printAdjacencyList(adjacencyList, villages);
    graph = initGraph(villages);
    fillGraph(graph, villages, freindlyVillages, saturation);
    queue = initQueue();
    if (!queue) {
        freeAdjacencyList(adjacencyList, villages);
        return 1;
    }
    bfs(saturation);
    printGraph(graph, villages);
    for (int i = 0; i < villages; ++i) {
        vertex_t *vertex = graph->vertices[i];
        if (!vertex->food) {
            villagesWith1++;
        }
        if (vertex->food > minProducts) {
            minProducts = vertex->food;
        }
    }
    freeAdjacencyList(adjacencyList, villages);
    freeQueue(queue);
    freeGraph(graph, villages);
    // printf("\nRESULT\n");
    printf("%d %d\n", minProducts, villagesWith1);
    clock_t end = clock();
    printf("TIME: %f\n", (double)((end - start) / CLOCKS_PER_SEC));
    return ret;
}

void bfs(int maxSaturation) {
    // printf("Vertex %d is viseted!\n", to + 1);
    graph->vertices[0]->isVisited = true;
    push(queue, graph->vertices[0]);
    while (!isEmpty(queue)) {
        vertex_t *vertex = pop(queue);
        for (int i = 0; i < NUMBER_OF_NEIGHBOURS; ++i) {
            vertex_t *neighbour = getNeighbour(vertex->number, i);
            if (neighbour) {
                if (!neighbour->isVisited) {
                    // printf("Vertex %d is visted!\n", neighbour->number);
                    neighbour->isVisited = true;
                    neighbour->food = vertex->food;
                    neighbour->saturation = vertex->saturation - 1;
                    if (neighbour->saturation < 0) {
                        neighbour->food++;
                        neighbour->saturation = maxSaturation;
                    }
                    if (neighbour->isFriendly) {
                        neighbour->saturation = maxSaturation;
                    }
                    // if (!neighbour->food) {
                    //     villagesWith1++;
                    // }
                    // if (neighbour->food > minProducts) {
                    //     minProducts = neighbour->food;
                    // }
                    // if (neighbour->number == 11 && neighbour->food == 2) {
                    //     printf("FROM: %d %d %d\n", vertex->number, vertex->food, vertex->saturation);
                    // }
                    push(queue, neighbour);
                } else {
                    int tmpFood = vertex->food;
                    int tmpSaturation = vertex->saturation - 1;
                    if (tmpSaturation < 0) {
                        tmpFood++;
                        tmpSaturation = maxSaturation;
                    }
                    // if (vertex->number == 10 && neighbour->number == 11) {
                    //     printf("FROM2: %d %d\n", tmpFood, tmpSaturation);
                    // }
                    if (neighbour->food > tmpFood ||
                        (neighbour->food == tmpFood && neighbour->saturation < tmpSaturation)) {
                        // if (minProducts <= neighbour->food) {
                        //     minProducts = tmpFood;
                        // }
                        neighbour->food = tmpFood;
                        neighbour->saturation = tmpSaturation;
                        // if (!neighbour->food) {
                        //     villagesWith1++;
                        // }
                        if (neighbour->isFriendly) {
                            neighbour->saturation = maxSaturation;
                        }
                        for (int i = 0; i < 4; ++i) {
                            vertex_t *vert = getNeighbour(neighbour->number, i);
                            if (vert) {
                                push(queue, vert);
                            } else {
                                break;
                            }
                        }
                    }
                }
            } else {
                break;
            }
        }
    }
}

vertex_t *getNeighbour(int parent, int index) {
    vertex_t *vertex = NULL;
    for (int i = index; i < NUMBER_OF_NEIGHBOURS; ++i) {
        int childNumber = adjacencyList[parent][i];
        if (childNumber > 0) {
            vertex = graph->vertices[childNumber - 1];
            break;
        }
    }
    return vertex;
}
