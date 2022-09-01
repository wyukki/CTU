#include "utils.h"

typedef struct node {
    struct node *left;
    struct node *right;
    int number;
    int depth;
} node_t;

node_t* addToTreeIterative(node_t *node, int value);
node_t *addToTree(node_t *node, int startOfInterval, int endOfInterval);
void printPreorder(node_t *node, node_t *parent, int depth);
void freeTree(node_t *root);
node_t *minValueNode(node_t *node);
node_t* deleteNodeIterative(node_t* root, int value);
int treeMaxDepth(const node_t *const node);
node_t* deleteNode(node_t* root, int startOfInterval, int endOfInterval);
void countVertices(node_t *root, int *counter);
