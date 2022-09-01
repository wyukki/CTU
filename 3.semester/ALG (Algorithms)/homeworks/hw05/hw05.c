#include "tree.h"
#include "utils.h"

void fillVertices(bool *vertices, int typeOfOperation, int start, int end);
int *trimVertices(bool *vertices);
void findNewRoot(bool *vertices);
void depthOnTheLeft(bool *vertices);

int **getAdjacencyList();

int main(int argc, char *argv[]) {
    int ret = EXIT_SUCCESS;
    int numberOfOperations;
    if ((scanf("%d\n", &numberOfOperations)) != 1) {
        fprintf(stderr, "Error: wrong number of operations!\n");
    }
    node_t *root = NULL;
    for (int i = 0; i < numberOfOperations; ++i) {
        char operationType;
        int startOfInterval;
        int endOfInterval;
        if ((scanf("%c %d %d\n", &operationType, &startOfInterval, &endOfInterval)) != 3) {
            fprintf(stderr, "Error: wrong input!\n");
            exit(1);
        }
        if (operationType == 'i') {
            root = addToTree(root, startOfInterval, endOfInterval);
        } else {
            root = deleteNode(root, startOfInterval, endOfInterval);
        }
    }
    int numberOfVertices = 0;
    countVertices(root, &numberOfVertices);
    printf("%d %d\n", numberOfVertices, treeMaxDepth(root) - 1);
    freeTree(root);
    return ret;
}
