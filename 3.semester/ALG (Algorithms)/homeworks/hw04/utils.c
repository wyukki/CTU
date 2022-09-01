#include "utils.h"


int **getAdjacencyList(int villages, int routes) {
    int **adjacencyList = malloc(sizeof(int *) * (villages + 1));
    if (!adjacencyList) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return NULL;
    }
    for (int i = 1; i <= villages; ++i) {
        adjacencyList[i] = calloc(NUMBER_OF_NEIGHBOURS + 1, sizeof(int));
        if (!adjacencyList[i]) {
            fprintf(stderr, alloc_error, __LINE__, __FILE__);
            free(adjacencyList);
            return NULL;
        }
    }
    for (int i = 0; i < routes; ++i) {
        int village1, village2;
        if ((scanf("%d %d\n", &village1, &village2)) != 2) {
            fprintf(stderr, "Error: wrong input in file %s\n", __FILE__);
            exit(-1);
        }
        addToAdjacencyList(adjacencyList, village1, village2);
        addToAdjacencyList(adjacencyList, village2, village1);
    }
    return adjacencyList;
}

void addToAdjacencyList(int **adjacencyList, int village1, int village2) {
    int lastIndex = adjacencyList[village1][NUMBER_OF_NEIGHBOURS];
    adjacencyList[village1][lastIndex] = village2;
    if (adjacencyList[village1][NUMBER_OF_NEIGHBOURS] > NUMBER_OF_NEIGHBOURS) {
        fprintf(stderr, "Village %d has more than 4 neighbours!\n", village1);
        exit(300);
    }
    adjacencyList[village1][NUMBER_OF_NEIGHBOURS]++;
}

void printAdjacencyList(int **adjacencyList, int villages) {
    for (int i = 1; i <= villages; ++i) {
        printf("Village %d has %d neighbours: ", i, adjacencyList[i][NUMBER_OF_NEIGHBOURS]);
        for (int j = 0; j < NUMBER_OF_NEIGHBOURS; ++j) {
            if (!adjacencyList[i][j]) {
                break;
            }
            printf("%d ", adjacencyList[i][j]);
        }
        printf("\n");
    }
}

void freeAdjacencyList(int **adjacencyList, int villages) {
    for (int i = 1; i <= villages; ++i) {
        free(adjacencyList[i]);
    }
    free(adjacencyList);
}
