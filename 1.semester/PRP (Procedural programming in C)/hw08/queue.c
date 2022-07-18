#include "queue.h"

#include <string.h>
#define SIZE 20

const char *const alloc_error = "Allocation error!\n";

queue_t *create_queue(int capacity) {
    queue_t *queue = malloc(sizeof(queue_t));
    if (!queue) {
        fprintf(stderr, alloc_error);
        free(queue);
        return NULL;
    }
    queue->size = 0;
    queue->capacity = capacity;
    queue->head_index = 0;
    queue->tail_index = 0;
    queue->arr = malloc(sizeof(void *) * capacity);
    for (int i = 0; i < capacity; ++i) {
        queue->arr[i] = NULL;
    }
    if (!queue->arr) {
        fprintf(stderr, alloc_error);
        free(queue->arr);
        return NULL;
    }
    return queue;
}
/* deletes the queue and all allocated memory */
void delete_queue(queue_t *queue) {
    if (!queue) return;
    free(queue->arr);
    free(queue);
}

/*
 * inserts a reference to the element into the queue
 * returns: true on success; false otherwise
 */
bool push_to_queue(queue_t *queue, void *data) {
    bool ret = false;
    if (queue) {
        if (queue->head_index == 0 && queue->tail_index == 0) {
            queue->arr[queue->tail_index] = data;
        } else if (queue->tail_index > 0) {
            if (queue->tail_index == queue->capacity) {
                queue->tail_index = 0;
            }
            queue->arr[queue->tail_index] = data;
        }
        queue->tail_index++;
        queue->size++;
        ret = true;
    }
    return ret;
}

/*
 * gets the first element from the queue and removes it from the queue
 * returns: the first element on success; NULL otherwise
 */
void *pop_from_queue(queue_t *queue) {
    void *ret = NULL;
    if (queue->tail_index > 0) {
        if (queue->head_index == queue->tail_index) {
            queue->head_index = 0;
            queue->tail_index = 0;
            return ret;
        }
        if (queue->head_index == queue->capacity) {
            queue->head_index = 0;
        }
        ret = queue->arr[queue->head_index];
        queue->arr[queue->head_index] = NULL;
        queue->head_index++;
        queue->size--;
    }
    return ret;
}

/*
 * gets idx-th element from the queue, i.e., it returns the element that
 * will be popped after idx calls of the pop_from_queue()
 * returns: the idx-th element on success; NULL otherwise
 */
void *get_from_queue(queue_t *queue, int idx) {
    void *ret = NULL;
    if (queue && idx >= 0 && idx < queue->capacity) {
        ret = queue->arr[idx];
    }
    return ret;
}

/* gets number of stored elements */
int get_queue_size(queue_t *queue) {
    return queue->size;
}
