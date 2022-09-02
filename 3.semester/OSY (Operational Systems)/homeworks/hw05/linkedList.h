#ifndef _LL_H
#define _LL_H
#include "queue.h"
#include "utils.h"

typedef struct entry {
    char *name;
    int workspace;
    pthread_t thread;
    struct entry *next;
    struct entry *prev;
    bool mustFinish;
} ll_entry_t;

typedef struct {
    ll_entry_t *head;
    ll_entry_t *tail;
    int size;
} ll_t;

ll_entry_t *allocateEntry(char *name, int workspace);
ll_t *initList();
void push(ll_t *list, ll_entry_t *entry);
void printList(ll_t *list);
void freeList(ll_t *list);
bool find(ll_t *list, int workspace);
void freeWorkers(ll_t *list, pthread_mutex_t *mustFinishMutex,
               int workplace, pthread_cond_t *workMutex);

#endif
