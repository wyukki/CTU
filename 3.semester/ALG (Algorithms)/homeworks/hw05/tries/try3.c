#include "tree.h"

node_t* initRoot() {
    node_t* root = (node_t*)malloc(sizeof(node_t));
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
        node_t *newNode = (node_t*)malloc(sizeof(node_t));
        newNode->left = NULL;
        newNode->right = NULL;
        newNode->number = value;
        node = newNode;
    }
    node->right = addToTheRight(node->right, value + 1, endOfInterval);
    return node;
}

node_t* addToTree(node_t* node, int startOfInterval, int endOfInterval) {
    if (!node) {
        node_t* firstChild = malloc(sizeof(node_t));
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
    if (startOfInterval > node->number) {
        node->right = addToTree(node->right, startOfInterval, endOfInterval);
    } else if (startOfInterval < node->number) {
        node->left = addToTree(node->left, startOfInterval, endOfInterval);
    }
    return node;
}

void printInorder(node_t* node) {
    if (!node) {
        return;
    }
    printInorder(node->left);
    printf("%d\n", node->number);
    printInorder(node->right);
}

void freeTree(node_t* root) {
    if (!root) {
        return;
    }
    freeTree(root->left);
    freeTree(root->right);
    free(root);
}

node_t* minValueNode(node_t* node) {
    node_t* curr = node;
    while (curr && curr->left != NULL)
        curr = curr->left;

    return curr;
}

node_t* deleteNode(node_t* root, int value) {
    if (root == NULL)
        return root;
    if (value < root->number) {
        root->left = deleteNode(root->left, value);
    } else if (value > root->number) {
        root->right = deleteNode(root->right, value);
    } else {
        if (root->left == NULL) {
            node_t* tmp = root->right;
            free(root);
            return tmp;
        } else if (root->right == NULL) {
            node_t* tmp = root->left;
            free(root);
            return tmp;
        }
        node_t* tmp = minValueNode(root->right);

        root->number = tmp->number;
        root->right = deleteNode(root->right, tmp->number);
    }
    return root;
}
