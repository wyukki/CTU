#ifndef UTILS_H
#define UTILS_H
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>
#include <semaphore.h>
#include <string.h>
#include <assert.h>
#define RUNTIME_ERROR 1

// ll = linked list 

typedef struct entry {
    int number; // value of entry
    char *text; // string in entry
    struct entry *next; // pointer to next element
} entry_t;

typedef struct {
    entry_t *head; // pointer to first element
    entry_t *tail; // pointer to last element
    int size; // size of ll
} ll_t;
/**
 * Creates and return pointer to ll
 */
ll_t *initList();
/**
 * Adds new element to ll
 */
void push(ll_t *list, int num, char *text);
/**
 * Removes element from ll
 */
entry_t *pop(ll_t *list);
/**
 * Prints whole ll
 */
void printList(ll_t *list);
/**
 * Frees memory
 */
void freeList(ll_t *list);
#endif
