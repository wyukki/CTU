#include "utils.h"

int **getOperations(int numberOfOperations) {
    int **operations = (int **)malloc(sizeof(int *) * numberOfOperations);
    if (!operations) {
        fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
        return NULL;
    }
    for (int i = 0; i < numberOfOperations; ++i) {
        operations[i] = malloc(sizeof(int) * NUMBER_OF_OPERANDS);
        if (!operations[i]) {
            fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
            free(operations);
            return NULL;
        }
        char operationType;
        if ((scanf("%c %d %d\n", &operationType, &operations[i][1], &operations[i][2])) != 3) {
            fprintf(stderr, "Error: wrong opearands!\n");
            free(operations);
            return NULL;
        }
        operations[i][0] = (int)(operationType);
    }
    return operations;
}

int getOperationType(int **operations, int operationNumber) {
    int ret = UNDEFINED_OPERATION;
    if (operations[operationNumber][0] == (int)'i') {
        ret = INSERT_OPERATION;
    } else if (operations[operationNumber][0] == (int)'d') {
        ret = DELETE_OPERATION;
    }
    return ret;
}

int getIntervalLength(int **operations, int operationNumber) {
    return operations[operationNumber][2] - operations[operationNumber][1];
}

void printOpearations(int **operations, int numberOfOperations) {
    for (int i = 0; i < numberOfOperations; ++i) {
        printf("Operation no. %d: type: %s, from: %d, to: %d\n", i,
               operations[i][0] == (int)'i' ? "insert" : "delete",
               operations[i][1], operations[i][2]);
    }
}

void freeOperations(int **opearations, int numberOfOpeartions) {
    if (!opearations) {
        return;
    }
    for (int i = 0; i < numberOfOpeartions; ++i) {
        free(opearations[i]);
    }
    free(opearations);
}
