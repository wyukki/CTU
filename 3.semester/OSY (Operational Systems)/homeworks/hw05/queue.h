#ifndef _QUEUE_H
#define _QUEUE_H

#include "products.h"
#include "utils.h"

static const char *const alloc_error = "Allocation error on line %d in file %s!\n";

typedef struct queueEntry {
    struct queueEntry *next;
    product_t *product;
} queue_entry_t;

typedef struct {
    queue_entry_t *head;
    queue_entry_t *tail;
    int size;
} queue_t;

queue_t *initQueue();

void pushToQueue(queue_t *queue, product_t *product);

queue_entry_t *popFromQueue(queue_t *queue);

void freeQueue(queue_t *queue);

bool queueIsEmpty(queue_t *queue);

queue_entry_t *hasRequest(queue_t *queue, int workplace);


#endif
