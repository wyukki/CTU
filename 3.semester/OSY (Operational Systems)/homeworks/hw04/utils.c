#include "utils.h"

/**
 *  Implementaion of linked list by Jan Faigl
 *  https://cw.fel.cvut.cz/b201/_media/courses/b0b36prp/lectures/b0b36prp-lec09-slides.pdf
 */

void printList(ll_t *list) {
    if (!list->size) {
        return;
    }
    entry_t *curr = list->head;
    while (curr) {
        printf("%d %s\n", curr->number, curr->text);
        curr = curr->next;
    }
}

ll_t *initList() {
    ll_t *list = malloc(sizeof(ll_t));
    if (!list) {
        fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
        return NULL;
    }
    list->head = NULL;
    list->tail = NULL;
    list->size = 0;
    return list;
}

void push(ll_t *list, int num, char *t) {
    if (!list) {
        fprintf(stderr, "The list object is not created!\n");
        return;
    }
    entry_t *newEntry = malloc(sizeof(entry_t));
    if (!newEntry) {
        fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
        exit(1);
    }
    newEntry->number = num;
    newEntry->text = t;
    newEntry->next = NULL;
    if (!list->tail) {
        list->head = list->tail = newEntry;
    } else {
        list->tail->next = newEntry;
        list->tail = newEntry;
    }
    list->size++;
    return;
}

entry_t *pop(ll_t *list) {
    if (!list || !list->tail) {
        return NULL;
    }
    entry_t *prev_head = list->head;
    list->head = prev_head->next;
    list->size--;
    if (!list->head) {
        list->tail = NULL;
    }
    return prev_head;
}

void freeList(ll_t *list){
    if (!list) {
        return;
    }
    entry_t *entry;
    while((entry = pop(list))){
        free(entry->text);
        free(entry);
    }
    free(list);
}

