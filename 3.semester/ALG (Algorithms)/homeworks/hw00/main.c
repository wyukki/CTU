#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#define MALLOC_SIZE 100
#define ALLOCATION_ERROR -1

void *my_malloc(int size) {
    void *mem = malloc(size);
    if (!mem) {
        fprintf(stderr, "Allocation error!\n");
        return NULL;
    }
    return mem;
}

void compare_number(int *numbers, int num);
bool is_in_duplicates(int *duplicates, int duplicates_len, int num);
void sort_duplicates(int *duplicates, int duplicates_len, int startIndex, int finalIndex);
void print_duplicates(int *duplicates, int duplicates_len);
int division(int *duplicates, int startIndex, int finalIndex);

/**
 * Program recieves sequnce of numbers, and finds duplicates in it.
 */

int main(int argc, char *argv[]) {
    int ret = EXIT_SUCCESS;
    int nums_malloc_coef = 1;
    int dupl_malloc_coef = 1;
    int *numbers = (int *)my_malloc(sizeof(int) * MALLOC_SIZE);
    int *duplicates = (int *)my_malloc(sizeof(int) * MALLOC_SIZE);
    if (!numbers || !duplicates) {
        ret = ALLOCATION_ERROR;
        return ret;
    }
    int r;
    int num = 0;
    int nums_len = 0;
    int dupl_len = 0;
    while ((r = scanf("%d ", &num)) != EOF) {
        if (nums_len == MALLOC_SIZE * nums_malloc_coef) {
            nums_malloc_coef++;
            int *tmp = realloc(numbers, MALLOC_SIZE * sizeof(int) * nums_malloc_coef);
            numbers = tmp;
        }
        if (dupl_len == MALLOC_SIZE * dupl_malloc_coef) {
            dupl_malloc_coef++;

            int *tmp = realloc(duplicates, MALLOC_SIZE * sizeof(int) * dupl_malloc_coef);
            duplicates = tmp;
        }
        if (!nums_len) {
            numbers[nums_len] = num;
            nums_len++;
        } else {
            for (int i = 0; i < nums_len; ++i) {
                if (numbers[i] == num) {
                    if (is_in_duplicates(duplicates, dupl_len, num)) {
                        break;
                    } else {
                        duplicates[dupl_len] = num;
                        dupl_len++;
                    }
                } else if (i == nums_len - 1) {
                    numbers[nums_len] = num;
                    nums_len++;
                    break;
                }
            }
        }
    }
    if (dupl_len != 1) {
        sort_duplicates(duplicates, dupl_len, 0, dupl_len - 1);
    }
    print_duplicates(duplicates, dupl_len);
    free(duplicates);
    free(numbers);
    return ret;
}

bool is_in_duplicates(int *duplicates, int duplicates_len, int num) {
    bool ret = false;
    for (int i = 0; i < duplicates_len; ++i) {
        if (duplicates[i] == num) {
            ret = true;
        }
    }
    return ret;
}
void sort_duplicates(int *duplicates, int duplicates_len,
                     int startIndex, int finalIndex) {
    if (startIndex < finalIndex) {
        int i = division(duplicates, startIndex, finalIndex);
        sort_duplicates(duplicates, duplicates_len, startIndex, i - 1);
        sort_duplicates(duplicates, duplicates_len, i + 1, finalIndex);
    }
}

void print_duplicates(int *duplicates, int duplicates_len) {
    for (int i = 0; i < duplicates_len; ++i) {
        printf("%d", duplicates[i]);
        if (i != duplicates_len - 1) {
            printf(" ");
        }
    }
    printf("\n");
}

int division(int *duplicates, int startIndex, int finalIndex) {
    int lastNumber = duplicates[finalIndex];
    int i = startIndex - 1;
    for (int j = startIndex; j < finalIndex; ++j) {
        if (duplicates[j] < lastNumber) {
            ++i;
            int tmp = duplicates[i];
            duplicates[i] = duplicates[j];
            duplicates[j] = tmp;
        }
    }
    ++i;
    int tmp = duplicates[i];
    duplicates[i] = duplicates[finalIndex];
    duplicates[finalIndex] = tmp;
    return i;
}
