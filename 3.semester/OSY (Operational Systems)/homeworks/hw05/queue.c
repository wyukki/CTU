#include "queue.h"

queue_t *initQueue() {
    queue_t *queue = (queue_t *)malloc(sizeof(queue_t));
    if (!queue) {
        fprintf(stderr, "Allocation error on line %d in file %s\n", __LINE__, __FILE__);
        return NULL;
    }
    queue->head = NULL;
    queue->tail = NULL;
    queue->size = 0;
    return queue;
}

void pushToQueue(queue_t *queue, product_t *product) {
    queue_entry_t *newEntry = malloc(sizeof(queue_entry_t));
    if (!newEntry) {
        fprintf(stderr, "Allocaton error on line %d in file %s\n", __LINE__, __FILE__);
        exit(1);
    }
    newEntry->product = product;
    newEntry->product->indexInQueue = queue->size;
    newEntry->next = NULL;
    if (queue->tail) {
        queue->tail->next = newEntry;
    } else {
        queue->head = newEntry;
    }
    queue->tail = newEntry;
    queue->size++;
}

queue_entry_t *popFromQueue(queue_t *queue) {
    queue_entry_t *ret = NULL;
    if (queue->head) {
        ret = queue->head;
        queue_entry_t *tmp = queue->head;
        queue->head = queue->head->next;
        free(tmp);
        if (queue->head == NULL) {
            queue->tail = NULL;
        }
        queue->size--;
    }
    return ret;
}

void freeQueue(queue_t *queue) {
    queue_entry_t *curr;
    while (queue->head){
        curr = queue->head;
        queue->head = queue->head->next;
        free(curr);
    }
    free(queue);
}

bool queueIsEmpty(queue_t* queue) {
    if (!queue) {
        return true;
    }
    return queue->size == 0;
}

queue_entry_t *hasRequest(queue_t *queue, int workplace) {
    if (!queue) {
        return NULL;
    }
    queue_entry_t *curr = queue->head;
    while (curr) {
        int step = curr->product->step;
        if (curr->product->steps[step] == workplace) {
            return curr;
        }
        curr = curr->next;
    }
    return NULL;
}

