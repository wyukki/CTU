#include "linkedList.h"

#include <errno.h>

extern int errno;

void printList(ll_t *list) {
    if (!list->size) {
        return;
    }
    ll_entry_t *curr = list->head;
    while (curr) {
        printf("%s %d\n", curr->name, curr->workspace);
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

ll_entry_t *allocateEntry(char *name, int workspace) {
    ll_entry_t *newEntry = (ll_entry_t *)malloc(sizeof(ll_entry_t));
    if (!newEntry) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return NULL;
    }
    newEntry->mustFinish = false;
    newEntry->name = name;
    newEntry->workspace = workspace;
    newEntry->next = NULL;
    newEntry->prev = NULL;
    return newEntry;
}

void push(ll_t *list, ll_entry_t *entry) {
    if (!list) {
        fprintf(stderr, "The list object is not created!\n");
        return;
    }
    if (!list->tail) {
        list->head = list->tail = entry;
    } else {
        entry->prev = list->tail;
        list->tail->next = entry;
        list->tail = entry;
    }
    list->size++;
    return;
}

void freeList(ll_t *list) {
    if (!list) {
        return;
    }
    ll_entry_t *tmp;
    while (list->head) {
        tmp = list->head;
        list->head = list->head->next;
        free(tmp->name);
        free(tmp);
    }
    free(list);
}

bool find(ll_t *list, int workplace) {
    if (!list->size) {
        return false;
    }
    ll_entry_t *curr = list->head;
    while (curr) {
        if (curr->workspace == workplace) {
            return true;
        }
        curr = curr->next;
    }
    return false;
}

void freeWorkers(ll_t *list, pthread_mutex_t *mustFinishMutex,
                 int workplace, pthread_cond_t *workMutex) {
    ll_entry_t *curr = list->head;
    while (curr) {
        if (curr->workspace == workplace) {
            pthread_mutex_lock(mustFinishMutex);
            curr->mustFinish = true;
            pthread_mutex_unlock(mustFinishMutex);
            pthread_cond_broadcast(workMutex);
            int retval;
            if ((retval = pthread_join(curr->thread, NULL))) {
                fprintf(stderr, "Error: join thread error %s!\n", strerror(retval));
                exit(1);
            }
        }
        curr = curr->next;
    }
}
