#include "tree.h"

const char *const alloc_error = "Allocation error on line %d in file %s!\n";

/**
 * Read input and create matrix of adjacent nodes.
 */
int **getAdjacencyMatrix(int numberOfVertices);
/**
 * Frees matrix of adjacent nodes.
 */
void freeAdjacencyMatrix(int **adjacencyMatrix, int numberOfVertices);
/**
 * Adds to matrix new node
 */
void addToMatrix(int **adjacencyMatrix, int index, int vertex);
/**
 * Prints matrix on stdout
 */
void printAdjacencyMatrix(int **adjacencyMatrix, int numberOfVertices);
/**
 * Finds first vertex in adjacency matrix, that has two "children".
 * That vertex will be the root in our tree.
 */
int findFirstVertexWithDegreeTwo(int **adjacencyMatrix, int numberOfVertices);

/**
 * We have a tree, where nodes are either white or red.
 * Goal is to traverse tree, and find that node, where the differnce
 * between left and right red nodes count will be the smallest.
 * 
 * Input is: first number (N) is a total nodes count, second (R) is count of red nodes.
 * First R numbers are always red nodes. Next N - 1 lines of input represents
 * two adjacent nodes (e.g. 1 2 means there is an edge between them). 
 * 
 * Assignment: https://cw.felk.cvut.cz/brute/data/ae/release/2021z_b4b33alg/alg_cz_2021z/evaluation/input.php?task=beads
 * (only in czech)
 */

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
