#include "tree.h"

result_t *result;

node_t *initTree(int allVertices) {
    node_t *tree = (node_t *)malloc(allVertices * sizeof(node_t));
    if (!tree) {
        fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
        return NULL;
    }
    tree->left = NULL;
    tree->right = NULL;
    tree->number = 0;
    tree->isRed = false;
    tree->counterOfRedBefore = 0;
    result = (result_t *)malloc(sizeof(result_t));
    if (!result) {
        fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
        exit(-1);
    }
    result->node1 = 0;
    result->node2 = 0;
    result->difference = __INT_MAX__;
    return tree;
}
/*
        1. Create a root
        2. Move to the first vertex, make him left child of root
        3. Recursivly repeat the process
        4. When there isn't a possible vertex to add, step back
        5. If parent of last built vertex has more children, then create them as a right child
        6. Recursivly repeat the proccess

*/
node_t *addToTree(node_t *node, int **adjacencyMatrix, int numberOfRedVertices,
                  int parent, int currVertex) {
    if (currVertex == -1) {
        return NULL;
    }
    if (!node) {
        node = (node_t *)malloc(sizeof(node_t));
        if (!node) {
            fprintf(stderr, "Allocation error on line %d in file %s", __LINE__, __FILE__);
            exit(1);
        }
        node->right = NULL;
        node->left = NULL;
    }
    node->isRed = isRed(currVertex, numberOfRedVertices);
    node->number = currVertex;
    node->counterOfRedBefore = 0;
    // printf("Vertex %d is added!\n", node->number);
    int child = getFirstChild(adjacencyMatrix, currVertex);
    if (child != -1) {
        // printf("The first child of vertex %d is %d\n", currVertex, child);
    }
    if (parent != -1) {
        removeFromAdjacencyMatrix(adjacencyMatrix, currVertex, parent);
    }
    if (!vertexHasAChild(adjacencyMatrix, currVertex)) {
        // printf("The vertex %d has no children!\n", currVertex);
        node->left = NULL;
        node->right = NULL;
        return node;
    }
    node->left = addToTree(node->left, adjacencyMatrix,
                           numberOfRedVertices, currVertex,
                           getFirstChild(adjacencyMatrix, currVertex));
    node->right = addToTree(node->right, adjacencyMatrix,
                            numberOfRedVertices, currVertex,
                            getFirstChild(adjacencyMatrix, currVertex));
    return node;
}

void removeFromAdjacencyMatrix(int **adjacencyMatrix, int parent, int child) {
    for (int i = 0; i < 3; ++i) {
        if (adjacencyMatrix[parent][i] == child) {
            adjacencyMatrix[parent][i] = -1;
            adjacencyMatrix[parent][3]--;
        }
        if (adjacencyMatrix[child][i] == parent) {
            adjacencyMatrix[child][i] = -1;
            adjacencyMatrix[child][3]--;
        }
    }
}

bool isRed(int numberOfVertex, int numberOfRedVertices) {
    return numberOfVertex <= numberOfRedVertices;
}

bool vertexHasAChild(int **adjacencyMatrix, int vertex) {
    return adjacencyMatrix[vertex][3];
}

int getFirstChild(int **adjacencyMatrix, int vertex) {
    int ret = -1;
    if (vertex == ret) {
        return ret;
    }
    for (int i = 0; i < 3; ++i) {
        if (adjacencyMatrix[vertex][i] > 0) {
            ret = adjacencyMatrix[vertex][i];
            break;
        }
    }
    return ret;
}

void printPostOrder(node_t *node) {
    if (!node) {
        return;
    }
    printPostOrder(node->left);
    printPostOrder(node->right);
    printf("%d ", node->number);
    if (node->isRed) {
        printf(" is red\n");
    } else {
        printf(" is white\n");
    }
}

void findResult(node_t *node, node_t *parent, int numberOfRedVertices) {
    if (!node) {
        return;
    }
    findResult(node->left, node, numberOfRedVertices);
    findResult(node->right, node, numberOfRedVertices);
    if (node->isRed) {
        node->counterOfRedBefore++;
    }
    if (parent) {
        int diff = abs((numberOfRedVertices - node->counterOfRedBefore) - node->counterOfRedBefore);
        if (diff < result->difference) {
            result->difference = diff;
            result->node1 = node->number > parent->number ? parent->number : node->number;
            result->node2 = node->number < parent->number ? parent->number : node->number;
        } else if (diff == result->difference) {
            int newResultNode1 = node->number > parent->number ? parent->number : node->number;
            int newResultNode2 = node->number < parent->number ? parent->number : node->number;
            // result->node1 = node->number > newResultNode1 ? newResultNode1 : node->number;
            // result->node2 = node->number < newResultNode2 ? newResultNode2 : node->number;
            result->node1 = result->node1 > newResultNode1 ? newResultNode1 : result->node1;
            result->node2 = result->node2 > newResultNode2 ? newResultNode2 : result->node2;
        }
        parent->counterOfRedBefore += node->counterOfRedBefore;
    }
}

void printResult() {
    printf("%d %d\n", result->node1, result->node2);
}
