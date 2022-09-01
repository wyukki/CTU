#include "tree.h"

node_t *initRoot() {
    node_t *root = (node_t *)malloc(sizeof(node_t));
    if (!root) {
        fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
        return NULL;
    }
    root->left = NULL;
    root->right = NULL;
    root->number = 0;
    return root;
}

node_t *addToTheRight(node_t *node, int value, int endOfInterval) {
    if (value > endOfInterval) {
        return NULL;
    }
    if (!node) {
        node_t *newNode = (node_t *)malloc(sizeof(node_t));
        newNode->left = NULL;
        newNode->right = NULL;
        newNode->number = value;
        node = newNode;
    }
    node->right = addToTheRight(node->right, value + 1, endOfInterval);
    return node;
}

node_t *addToTree(node_t *node, int startOfInterval, int endOfInterval) {
    if (!node) {
        node_t *firstChild = malloc(sizeof(node_t));
        if (!firstChild) {
            fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
            exit(1);
        }
        firstChild->number = startOfInterval;
        firstChild->left = NULL;
        firstChild->right = NULL;
        firstChild->right = addToTheRight(firstChild->right, startOfInterval + 1, endOfInterval);
        return firstChild;
    }
    if (startOfInterval > node->number && endOfInterval > node->number) {  // can insert whole interval to the right part
        node->right = addToTree(node->right, startOfInterval, endOfInterval);
    } else if (startOfInterval < node->number && endOfInterval < node->number) {  // can insert whole interval to the left part
        node->left = addToTree(node->left, startOfInterval, endOfInterval);
    } else if (startOfInterval < node->number && endOfInterval > node->number) {  // can insert only part of the interval
        node->left = addToTree(node->left, startOfInterval, node->number - 1);
        node->right = addToTree(node->right, node->number + 1, endOfInterval);
    } else if (startOfInterval == node->number && node->number < endOfInterval) {
        node->right = addToTree(node->right, node->number + 1, endOfInterval);
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

void findResult(node_t *node, int depth, int *max, int *numberOfVertices) {
    if (!node) {
        return;
    }
    if (*(max) < depth) {
        *(max) = depth;
    }
    depth++;
    *(numberOfVertices) += 1;
    findResult(node->left, depth, max, numberOfVertices);
    findResult(node->right, depth, max, numberOfVertices);
}

void printPreorder(node_t *node, int depth) {
    if (!node) {
        return;
    }
    printf("%d has depth %d\n", node->number, depth++);
    printPreorder(node->left, depth);
    printPreorder(node->right, depth);
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
    while (curr && curr->left != NULL)
        curr = curr->left;

    return curr;
}

node_t *deleteNodeIterative(node_t *root, int value) {
    if (root == NULL)
        return root;
    if (value < root->number) {
        root->left = deleteNodeIterative(root->left, value);
    } else if (value > root->number) {
        root->right = deleteNodeIterative(root->right, value);
    } else {
        if (root->left == NULL) {
            node_t *tmp = root->right;
            free(root);
            return tmp;
        } else if (root->right == NULL) {
            node_t *tmp = root->left;
            free(root);
            return tmp;
        }
        node_t* tmp = minValueNode(root->right);
        root->number = tmp->number;
        root->right = deleteNodeIterative(root->right, tmp->number);
    }
    return root;
}

// node_t *deleteNode(node_t *root, int value) {
node_t *deleteNode(node_t *root, int startOfInterval, int endOfInterval) {
    if (!root || startOfInterval > endOfInterval) {
        // fprintf(stderr, "The vertex number is not in the tree!\n");
        return root;
    } else if (root->number > startOfInterval) {
        root->left = deleteNode(root->left, startOfInterval, endOfInterval);
    } else if (startOfInterval > root->number) {
        root->right = deleteNode(root->right, startOfInterval, endOfInterval);
    } else if (startOfInterval < root->number && root->number <= endOfInterval) {
        root->left = deleteNode(root->left, startOfInterval, root->number - 1);
        root->right = deleteNode(root->right, root->number + 1, endOfInterval);
        root = deleteNode(root, root->number, root->number);
    } else if (startOfInterval == root->number) {
        if (!root->right && root->left) {
            free(root);
            root = NULL;
            return root;
        } else if (!root->left) {
            node_t *newLeft = root->right;
            free(root);
            root = NULL;
            // return newLeft;
            return deleteNode(newLeft, startOfInterval + 1, endOfInterval);
        } else if (!root->right) {
            node_t *newRight = root->left;
            free(root);
            root = NULL;
            return newRight;
        } else {
            root->right = deleteNode(root->right, root->number + 1, endOfInterval);
            if (!root->right) {
                return deleteNode(root, startOfInterval, startOfInterval);
            }
            // node_t *newRoot = minValueNode(root->right);
            root->number = minValueNode(root->right)->number;
            root->right = deleteNode(root->right, root->number, root->number);
        }
    }
    return root;
}

node_t *delete (node_t *root, int startOfInterval, int endOfInterval) {
    if (startOfInterval > endOfInterval || !root) {
        return root;
    } else if (startOfInterval > root->number) {
        root->right = delete (root->right, startOfInterval, endOfInterval);
    } else if (startOfInterval < root->number && root->number <= endOfInterval) {
        root->right = delete (root->right, root->number + 1, endOfInterval);
        root->left = delete (root->left, startOfInterval, root->number - 1);
        root = delete (root, root->number, root->number);
    } else if (startOfInterval < root->number) {
        root->left = delete (root->left, startOfInterval, endOfInterval);
    } else if (startOfInterval == root->number) {
        if (!root->right && !root->left) {
            root = NULL;
            return root;
        } else if (!root->right) {
            node_t *tmp = root->left;
            root = NULL;
            return tmp;
        } else if (!root->left) {
            node_t *tmp = root->right;
            root = NULL;
            return delete (tmp, startOfInterval + 1, endOfInterval);
        } else {
            root->right = delete (root->right, root->number + 1, endOfInterval);
            if (!root->right) {
                return delete (root, startOfInterval, startOfInterval);
            }
            root->number = minValueNode(root->right)->number;
            root->right = delete (root->right, root->number, root->number);
        }
    }
    return root;
}
