#include "utils.h"

void fillVertices(bool *vertices, int typeOfOperation, int start, int end);
int *trimVertices(bool *vertices);
void findNewRoot(bool *vertices);
int numberOfVertices = 0;
int root = 0;
int depth = 0;

int main(int argc, char *argv[]) {
    int ret = EXIT_SUCCESS;
    int numberOfOperations;
    if ((scanf("%d\n", &numberOfOperations)) != 1) {
        fprintf(stderr, "Error: wrong number of operations!\n");
    }
    int **operations = getOperations(numberOfOperations);
    if (!operations) {
        return -1;
    }
    bool *vertices = (bool *)calloc(MAX_NUMBER_OF_VERTICES + 1, sizeof(bool));
    if (!vertices) {
        fprintf(stderr, "Allocation error on line %d in file %s\n", __LINE__, __FILE__);
        freeOperations(operations, numberOfOperations);
        return EXIT_FAILURE;
    }
    root = operations[0][1];
    for (int i = 0; i < numberOfOperations; ++i) {
        int typeOfOperation = operations[i][0];
        int startOfInterval = operations[i][1];
        int endOfInterval = operations[i][2];
        fillVertices(vertices, typeOfOperation, startOfInterval, endOfInterval);
    }
    // printf("Number of vertices %d\n", numberOfVertices);
    int *finVertices = trimVertices(vertices);
    // printf("Vertices in the end:\n");
    // for (int i = 0; i < numberOfVertices; ++i) {
    //     printf("%d\n", finVertices[i]);
    // }
    int depthOnTheLeft = 0;
    int depthOnTheRight = 0;
    for (int i = 0; i < numberOfVertices; ++i) {
        if (finVertices[i] != root) {
            // printf("finVertices[%d]=%d\n", i, finVertices[i]);
            depthOnTheLeft++;
        } else {
            break;
        }
    }
    // printf("Root in the end = %d\n", root);
    depthOnTheRight = numberOfVertices - 1 - depthOnTheLeft;
    // printf("Right = %d, left = %d\n", depthOnTheRight, depthOnTheLeft);
    depth = depthOnTheLeft > depthOnTheRight ? depthOnTheLeft : depthOnTheRight;
    printf("%d %d\n", numberOfVertices, depth);
    free(vertices);
    free(finVertices);
    // printOpearations(operations, numberOfOperations);
    freeOperations(operations, numberOfOperations);
    return ret;
}
void fillVertices(bool *vertices, int typeOfOperation, int start, int end) {
    if (typeOfOperation == (int)'i') {
        // printf("insert!\n");
        for (int i = start; i <= end; i++) {
            numberOfVertices = !vertices[i] ? numberOfVertices + 1 : numberOfVertices;
            vertices[i] = true;
        }
    } else {
        // printf("delete!\n");
        for (int i = start; i <= end; i++) {
            numberOfVertices = vertices[i] ? numberOfVertices - 1 : numberOfVertices;
            vertices[i] = false;
            if (i == root) {
                findNewRoot(vertices);
            }
        }
    }
}
int *trimVertices(bool *vertices) {
    int *finalVertices = (int *)malloc(sizeof(int) * numberOfVertices);
    if (!finalVertices) {
        fprintf(stderr, "Allocation error on line %d in file %s\n", __LINE__, __FILE__);
        return NULL;
    }
    int len = 0;
    for (int i = 1; i < MAX_NUMBER_OF_VERTICES; ++i) {
        if (vertices[i]) {
            finalVertices[len++] = i;
        }
    }
    return finalVertices;
}

void findNewRoot(bool *vertices) {
    bool found = false;
    for (int i = root + 1; i < MAX_NUMBER_OF_VERTICES; ++i) {
        if (vertices[i]) {
            root = i;
            found = true;
            break;
        }
    }
    if (!found) {
        for (int i = root - 1; i >= 0; --i) {
            if (vertices[i]) {
                root = i;
                break;
            }
        }
    }
}
