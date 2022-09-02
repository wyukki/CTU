#include "tree.h"
#include "utils.h"

/**
 * Assume we have classical BST.
 * Goal is to implement interval add and delete operation.
 *
 * Assignment: https://cw.felk.cvut.cz/brute/data/ae/release/2021z_b4b33alg/alg_cz_2021z/evaluation/input.php?task=intervalBVS
 * (only in czech)
 */
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
