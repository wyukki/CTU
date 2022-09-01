
#include "queue.h"

queue_t *initQueue() {
    queue_t *queue = (queue_t *)malloc(sizeof(queue_t));
    if (!queue) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return NULL;
    }
    queue->head = NULL;
    queue->tail = NULL;
    queue->size = 0;
    return queue;
}
void push(queue_t *queue, vertex_t *vertex) {
    queueEntry_t *newEntry = (queueEntry_t *)malloc(sizeof(queueEntry_t));
    if (!newEntry) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return;
    }
    newEntry->vertex = vertex;
    newEntry->next = NULL;
    if (queue->tail) {
        queue->tail->next = newEntry;
    } else {
        queue->head = newEntry;
    }
    queue->tail = newEntry;
    queue->size++;
}

void pushHead(queue_t *queue, vertex_t *vertex){
    if (!queue || !queue->head) {
        return;
    }
    queueEntry_t *newHead = (queueEntry_t *)malloc(sizeof(queueEntry_t));
    if (!newHead) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return;
    }
    newHead->vertex = vertex;
    newHead->next = queue->head;
    queue->head = newHead;
    queue->size++;
}

vertex_t *pop(queue_t *queue) {
    vertex_t *ret = NULL;
    if (queue->head) {
        ret = queue->head->vertex;
        queueEntry_t *tmp = queue->head;
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
    vertex_t *curr;
    while ((curr = pop(queue))) {
        free(curr);
    }
    free(queue);
}

bool isEmpty(queue_t *queue) {
    return queue->size == 0;
}

bool isInQueue(queue_t *queue, vertex_t *vertex){
    if (!queue || !queue->head) {
        return false;
    }
    queueEntry_t *entry = queue->head;
    while (entry) {
        if (entry->vertex->number == vertex->number) {
            return true;
        }
        entry = entry->next;
    }
    return false;
}
