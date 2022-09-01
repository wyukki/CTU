#include "graph.h"
#include "queue.h"
#include "utils.h"

queue_t *queue;
graph_t *graph;
int **adjacencyList;
int minPackages = 0;
int villagesWithZeroPackages = 0;
/**
 * Return the index'th neighbour of vertex parent
 */
vertex_t *getNeighbour(int parent, int index);
void bfs(int maxSaturation);

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    int villages, routes, freindlyVillages, saturation;
    if ((scanf("%d %d %d %d\n", &villages, &routes, &freindlyVillages, &saturation)) != 4) {
        fprintf(stderr, "Error: wrong input!\n");
        return -1;
    }
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
    // printGraph(graph, villages);
    for (int i = 0; i < villages; ++i) {
        vertex_t *vertex = graph->vertices[i];
        if (!vertex->food) {
            villagesWithZeroPackages++;
        }
        if (vertex->food > minPackages) {
            minPackages = vertex->food;
        }
    }
    freeAdjacencyList(adjacencyList, villages);
    freeQueue(queue);
    freeGraph(graph, villages);
    printf("%d %d\n", minPackages, villagesWithZeroPackages);
    return ret;
}

void bfs(int maxSaturation) {
    graph->vertices[0]->isVisited = true;
    push(queue, graph->vertices[0]);
    while (!isEmpty(queue)) {
        vertex_t *vertex = pop(queue);
        for (int i = 0; i < NUMBER_OF_NEIGHBOURS; ++i) {
            vertex_t *neighbour = getNeighbour(vertex->number, i);
            if (neighbour) {
                if (!neighbour->isVisited) {
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
                    push(queue, neighbour);
                } else {
                    int tmpFood = vertex->food;
                    int tmpSaturation = vertex->saturation - 1;
                    if (tmpSaturation < 0) {
                        tmpFood++;
                        tmpSaturation = maxSaturation;
                    }
                    if (neighbour->food > tmpFood ||
                        (neighbour->food == tmpFood && neighbour->saturation < tmpSaturation)) {
                        neighbour->food = tmpFood;
                        neighbour->saturation = tmpSaturation;
                        if (neighbour->isFriendly) {
                            neighbour->saturation = maxSaturation;
                        }
                        // if (!isInQueue(queue, neighbour)) {
                            pushHead(queue, neighbour);
                        // }
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
