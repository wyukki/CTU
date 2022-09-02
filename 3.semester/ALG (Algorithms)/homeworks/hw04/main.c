#include "graph.h"
#include "queue.h"
#include "utils.h"

queue_t *queue; // global, because can take huge amount of memory
graph_t *graph; // global, because can take huge amount of memory
int **adjacencyList; // global, because can take huge amount of memory
int minPackages = 0; // global because is used in recursion
int villagesWithZeroPackages = 0;

/**
 * Return the index'th neighbour of vertex parent
 */
vertex_t *getNeighbour(int parent, int index);
void bfs(int maxSaturation);
/**
 * We are in desert, that we're going to represnt as graph.
 * Each node in graph is a small village, that can be friednly or not.
 * On the example image in directory "data" frindly villages are green.
 * We have packages of food, that we can use. If we are in friendly village,
 * out saturation (i.e. number of packages) is always maximal (packages = S).
 * If we travel to not friendly village, we need to use one of our packages.
 *
 * Goal is to compute two numbers: first is count of minimal packages, that
 * we need to get to every village, secons is a number of villages,
 * that we can visit without spending our packages.
 * 
 * Input is:
 * 1. 4 numbers (N, M, S, D), where N is a villges count, M is a count of
 * routes between 2 villages, S - count of friendly villages, D - value of
 * saturation in frienldy villages. Villages have numbers between 1 to N.
 * Init village always has number 1. Friendly villages has numbers between 1 and S.
 * 2. M lines, that represents route between village V1 and V2
 * 
 * Assignment: https://cw.felk.cvut.cz/brute/data/ae/release/2021z_b4b33alg/alg_cz_2021z/evaluation/input.php?task=caravan
 * (only in czech)
 */
int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    int villages, routes, freindlyVillages, saturation;
    if ((scanf("%d %d %d %d\n", &villages, &routes, &freindlyVillages, &saturation)) != 4) {
        fprintf(stderr, "Error: wrong input!\n");
        return -1;
    }
    adjacencyList = getAdjacencyList(villages, routes);
    graph = initGraph(villages);
    fillGraph(graph, villages, freindlyVillages, saturation);
    queue = initQueue();
    if (!queue) {
        freeAdjacencyList(adjacencyList, villages);
        return 1;
    }
    bfs(saturation);
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
                        pushHead(queue, neighbour);
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
