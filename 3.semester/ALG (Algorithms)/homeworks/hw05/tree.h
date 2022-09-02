#include "utils.h"

typedef struct node {
    struct node *left;
    struct node *right;
    int number;
    int depth;
} node_t;
/**
 * Classic way to add new node to tree.
 * Was used for debugging.
 */
node_t* addToTreeIterative(node_t *node, int value);
/**
 * Adds to tree new nodes in interval [startOfInterval, endOfInterval]
 */
node_t *addToTree(node_t *node, int startOfInterval, int endOfInterval);
/**
 * Prints tree in pre order
 */
void printPreorder(node_t *node, node_t *parent, int depth);
/**
 * Frees tree structure
 */
void freeTree(node_t *root);
/**
 * Returns node, that has minimal value
 */
node_t *minValueNode(node_t *node);
/**
 * Deletes node in classical iterative way.
 * Was used for debugging
 */
node_t* deleteNodeIterative(node_t* root, int value);
/**
 * Computes maximal depth in tree.
 */
int treeMaxDepth(const node_t *const node);
/**
 * Deletes nodes in interval [startOfInterval, endOfInterval]
 */
node_t* deleteNode(node_t* root, int startOfInterval, int endOfInterval);
/**
 * Returns number of nodes on tree.
 */
void countVertices(node_t *root, int *counter);
