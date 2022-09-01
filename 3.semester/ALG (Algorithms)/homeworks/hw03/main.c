#include "tree.h"

const char *const alloc_error = "Allocation error on line %d in file %s!\n";

int **getAdjacencyMatrix(int numberOfVertices);
void freeAdjacencyMatrix(int **adjacencyMatrix, int numberOfVertices);
void addToMatrix(int **adjacencyMatrix, int index, int vertex);
void printAdjacencyMatrix(int **adjacencyMatrix, int numberOfVertices);
int findFirstVertexWithDegreeTwo(int **adjacencyMatrix, int numberOfVertices);

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    int numberOfVertices, numberOfRedVertices;
    if ((scanf("%d %d\n", &numberOfVertices, &numberOfRedVertices)) != 2) {
        fprintf(stderr, "Error: wrong input!\n");
        return 1;
    }
    int **adjacencyMatrix = getAdjacencyMatrix(numberOfVertices);
    if (!adjacencyMatrix) {
        return 1;
    }
    int startIndex = findFirstVertexWithDegreeTwo(adjacencyMatrix, numberOfVertices);
    node_t *root = initTree(numberOfVertices);
    addToTree(root, adjacencyMatrix, numberOfRedVertices, -1, startIndex);
    freeAdjacencyMatrix(adjacencyMatrix, numberOfVertices);
    findResult(root, NULL, numberOfRedVertices);
    printResult();

    return ret;
}

int **getAdjacencyMatrix(int numberOfVertices) {
    int **adjacencyMatrix = malloc(sizeof(int *) * (numberOfVertices + 1));
    int v1, v2;
    if (!adjacencyMatrix) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return NULL;
    }
    for (int i = 1; i < numberOfVertices + 1; ++i) {
        adjacencyMatrix[i] = calloc(4, sizeof(int));
        if (!adjacencyMatrix[i]) {
            fprintf(stderr, alloc_error, __LINE__, __FILE__);
            free(adjacencyMatrix);
            return NULL;
        }
    }
    for (int i = 1; i < numberOfVertices; ++i) {
        if ((scanf("%d %d\n", &v1, &v2)) != 2) {
            fprintf(stderr, "Error: wrong input in adjacencyMatrix!\n");
            free(adjacencyMatrix);
            return NULL;
        }
        addToMatrix(adjacencyMatrix, v1, v2);
        addToMatrix(adjacencyMatrix, v2, v1);
    }
    return adjacencyMatrix;
}

void printAdjacencyMatrix(int **adjacencyMatrix, int numberOfVertices) {
    for (int i = 1; i < numberOfVertices + 1; ++i) {
        printf("Vertex %d has %d neighbors: ", i, adjacencyMatrix[i][3]);
        for (int j = 0; j < 3; ++j) {
            if (adjacencyMatrix[i][j] > 0) {
                printf("%d ", adjacencyMatrix[i][j]);
            }
        }
        printf("\n");
    }
}

void addToMatrix(int **adjacencyMatrix, int index, int vertex) {
    if (!adjacencyMatrix) {
        return;
    }
    if (adjacencyMatrix[index][0]) {
        if (adjacencyMatrix[index][1]) {
            adjacencyMatrix[index][2] = vertex;
        } else {
            adjacencyMatrix[index][1] = vertex;
        }
    } else {
        adjacencyMatrix[index][0] = vertex;
    }
    adjacencyMatrix[index][3]++;
}

int findFirstVertexWithDegreeTwo(int **adjacencyMatrix, int numberOfVertices) {
    int index = 0;
    for (int i = 1; i < numberOfVertices + 1; ++i) {
        if (adjacencyMatrix[i][3] == 2) {
            index = i;
            break;
        }
    }
    return index;
}
void freeAdjacencyMatrix(int **adjacencyMatrix, int numberOfVertices) {
    for (int i = 1; i < numberOfVertices + 1; ++i) {
        free(adjacencyMatrix[i]);
    }
    free(adjacencyMatrix);
}
