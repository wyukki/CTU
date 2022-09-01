#include "tree.h"

node_t *addToTreeIterative(node_t *node, int value) {
    if (!node) {
        node_t *newNode = malloc(sizeof(node_t));
        if (!newNode) {
            fprintf(stderr, "Allocation error on line %d in file %s\n", __LINE__, __FILE__);
            exit(1);
        }
        newNode->number = value;
        newNode->right = NULL;
        newNode->left = NULL;
        return newNode;
    }
    if (value > node->number) {
        node->right = addToTreeIterative(node->right, value);
    } else if (value < node->number) {
        node->left = addToTreeIterative(node->left, value);
    }
    return node;
}
node_t *addToTree(node_t *node, int startOfInterval, int endOfInterval) {
    if (startOfInterval > endOfInterval && !node) {
        return NULL;
    }
    if (!node) {
        node = (node_t *)malloc(sizeof(node_t));
        if (!node) {
            fprintf(stderr, "Allocation error on line %d in file %s\n", __LINE__, __FILE__);
            exit(-1);
        }
        node->left = NULL;
        node->right = NULL;
        node->number = startOfInterval;
        node->right = addToTree(node->right, startOfInterval + 1, endOfInterval);
    } else {
        if (endOfInterval < node->number) {
            node->left = addToTree(node->left, startOfInterval, endOfInterval);
        } else if (startOfInterval > node->number) {
            node->right = addToTree(node->right, startOfInterval, endOfInterval);
        } else if (startOfInterval <= node->number && node->number < endOfInterval) {
            node->left = addToTree(node->left, startOfInterval, node->number - 1);
            node->right = addToTree(node->right, node->number + 1, endOfInterval);
        } else if (endOfInterval == node->number && startOfInterval < node->number) {
            node->left = addToTree(node->left, startOfInterval, node->number - 1);
        }
    }
    return node;
}

int treeMaxDepth(const node_t *const node) {
    if (node) {
        const int left_depth = treeMaxDepth(node->left);
        const int right_depth = treeMaxDepth(node->right);
        return left_depth > right_depth ? left_depth + 1 : right_depth + 1;
    } else {
        return 0;
    }
}

void printPreorder(node_t *node, node_t *parent, int depth) {
    if (!node) {
        return;
    }
    printf("%d [depth = %d]", node->number, ++depth);
    if (parent) {
        printf(" [parent %d]\n", parent->number);
    } else {
        printf("\n");
    }
    printPreorder(node->left, node, depth);
    printPreorder(node->right, node, depth);
}

void freeTree(node_t *root) {
    if (!root) {
        return;
    }
    freeTree(root->left);
    freeTree(root->right);
    free(root);
}

node_t *minValueNode(node_t *node) {
    node_t *curr = node;
    while (curr->left != NULL) {
        curr = curr->left;
    }
    return curr;
}

node_t *deleteNodeIterative(node_t *root, int value) {
    if (root == NULL) {
        return root;
    }
    if (value < root->number) {
        root->left = deleteNodeIterative(root->left, value);
    } else if (value > root->number) {
        root->right = deleteNodeIterative(root->right, value);
    } else {
        if (root->left == NULL) {
            node_t *tmp = root->right;
            free(root);
            root = NULL;
            return tmp;
        } else if (root->right == NULL) {
            node_t *tmp = root->left;
            free(root);
            root = NULL;
            return tmp;
        }
        node_t *tmp = minValueNode(root->right);
        root->number = tmp->number;
        root->right = deleteNodeIterative(root->right, tmp->number);
    }
    return root;
}

node_t *deleteNode(node_t *node, int startOfInterval, int endOfInterval) {
    if (startOfInterval > endOfInterval || !node) {
        return NULL;
    }
    if (endOfInterval <= node->number) {
        node->left = deleteNode(node->left, startOfInterval, endOfInterval);
    } else if (startOfInterval >= node->number) {
        node->right = deleteNode(node->right, startOfInterval, endOfInterval);
    } else if (startOfInterval <= node->number && node->number <= endOfInterval) {
        node->left = deleteNode(node->left, startOfInterval, node->number);
        node->right = deleteNode(node->right, node->number, endOfInterval);
    }
    if (startOfInterval <= node->number && node->number <= endOfInterval) {
        if (!node->left) {
            node_t *newLeft = node->right;
            free(node);
            node = NULL;
            return newLeft;
        } else if (!node->right) {
            node_t *newRight = node->left;
            free(node);
            node = NULL;
            return newRight;
        }
        node_t *newRoot = minValueNode(node->right);
        node->number = newRoot->number;
        node->right = deleteNode(node->right, newRoot->number, newRoot->number);
    }
    return node;
}

void countVertices(node_t *root, int *counter) {
    if (!root) {
        return;
    }
    countVertices(root->left, counter);
    countVertices(root->right, counter);
    *(counter) = *(counter) + 1;
}
