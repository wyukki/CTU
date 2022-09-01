#include <stdio.h>
#include <stdlib.h>
#include <time.h>
typedef struct entry {
    int row;
    int column;
    struct entry *next;
} entry_t;

typedef struct {
    entry_t *head;
    entry_t *tail;
    int size;
} linked_list_t;

linked_list_t *initList();
entry_t *createEntry(int row, int column);
void push(linked_list_t *list, int row, int column);
void freeList(linked_list_t *list);
int **createMatrix(linked_list_t *list, int width);
void printMatrix(int **matrix, int width, int heigth);
void freeMatrix(int **matrix, int width, int height);
int max_height = 0;

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    clock_t start = clock();
    int width, eggs_count;
    if ((scanf("%d %d\n", &width, &eggs_count)) != 2) {
        fprintf(stderr, "Error: wrong input!\n");
        return EXIT_FAILURE;
    }
    printf("Number of positions: %d\n", width - 1);
    printf("Number of eggs: %d\n", eggs_count);
    linked_list_t *list = initList();
    if (!list) {
        return EXIT_FAILURE;
    }
    for (int i = 0; i < eggs_count; ++i) {
        int row, column;
        if ((scanf("%d %d\n", &column, &row)) != 2) {
            fprintf(stderr, "Error: wrong input!\n");
            return EXIT_FAILURE;
        }
        push(list, row, column);
        if (max_height < row) {
            max_height = row;
        }
    }
    printf("Max height = %d\n", max_height);
    int **matrix = createMatrix(list, width);
    printMatrix(matrix, width, max_height);
    freeMatrix(matrix, width, max_height);
    matrix = NULL;
    clock_t end = clock();
    printf("TIME: %f\n", (double)(end - start) / CLOCKS_PER_SEC);
    return ret;
}

linked_list_t *initList() {
    linked_list_t *list = (linked_list_t *)malloc(sizeof(linked_list_t));
    if (!list) {
        fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
        return NULL;
    }
    list->head = NULL;
    list->tail = NULL;
    list->size = 0;
    return list;
}
entry_t *createEntry(int row, int column) {
    entry_t *new_entry = (entry_t *)malloc(sizeof(entry_t));
    if (!new_entry) {
        fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
        return NULL;
    }
    new_entry->row = row;
    new_entry->column = column;
    new_entry->next = NULL;
    return new_entry;
}
void push(linked_list_t *list, int row, int column) {
    if (!list) {
        return;
    }
    entry_t *new_entry = createEntry(row, column);
    if (!list->head) {
        list->head = new_entry;
    } else {
        list->tail->next = new_entry;
    }
    list->tail = new_entry;
    list->size++;
}

void freeList(linked_list_t *list) {
    if (!list) {
        return;
    }
    entry_t *curr = list->head;
    while (curr) {
        entry_t *tmp = curr;
        curr = curr->next;
        free(tmp);
        tmp = NULL;
    }
    free(list);
    list = NULL;
}
int **createMatrix(linked_list_t *list, int width) {
    int **matrix = (int **)malloc(sizeof(int *) * (max_height + 1));
    if (!matrix) {
        fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
        return NULL;
    }
    for (int i = 0; i < max_height + 1; ++i) {
        matrix[i] = (int *)calloc(width, sizeof(int));
        if (!matrix[i]) {
            fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
            return NULL;
        }
    }
    entry_t *curr = list->head;
    for (int i = 0; i < list->size; ++i) {
        // printf("row: %d, column: %d\n", curr->row, curr->column);
        matrix[max_height - curr->row][curr->column - 1] = 1;
        curr = curr->next;
    }
    freeList(list);
    list = NULL;
    return matrix;
}

void printMatrix(int **matrix, int width, int heigth) {
    for (int i = 0; i < heigth + 1; ++i) {
        //printf("STEP %2d: ", max_height - i);
        for (int j = 0; j < width; ++j) {
            printf("%d ", matrix[i][j]);
        }
        printf("\n");
    }
}
void freeMatrix(int **matrix, int width, int height) {
    if (!matrix) {
        return;
    }
    for (int i = 0; i < height + 1; ++i) {
        free(matrix[i]);
    }
    free(matrix);
}
