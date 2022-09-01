#ifndef _QUEUE_H
#define _QUEUE_H
#include "utils.h"
#include "graph.h"

/**
 * Implementaion of queue by Jan Faigl:
 * https://cw.fel.cvut.cz/b201/_media/courses/b0b36prp/lectures/b0b36prp-lec11-slides.pdf
 */

typedef struct queueEntry {
    vertex_t *vertex;
    struct queueEntry *next;
} queueEntry_t;

typedef struct queue {
    queueEntry_t *head;
    queueEntry_t *tail;
    int size;
} queue_t;

/**
 * Creates and returns queue object
 */
queue_t *initQueue();
/**
 * Pushes the vertex object to queue
 */
void push(queue_t *queue, vertex_t *vertex);
void pushHead(queue_t *queue, vertex_t *vertex);
/**
 * Pops vertex object from queue
 */
vertex_t *pop(queue_t *queue);
/**
 * Returns true if there is not vertex in a queue
 */
bool isEmpty(queue_t *queue);
/**
 * Returns true if vertex is already in a queue
 */
bool isInQueue(queue_t *queue, vertex_t *vertex);
/**
 * Frees a queue object 
 */
void freeQueue(queue_t *queue);

#endif
