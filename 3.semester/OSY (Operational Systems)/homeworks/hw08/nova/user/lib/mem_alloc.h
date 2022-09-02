#ifndef MEM_ALLOC_H
#define MEM_ALLOC_H

/*
 * Memory allocator interface for
 * http://osy.pages.fel.cvut.cz/docs/cviceni/lab11/
 */

#ifdef __cplusplus
extern "C" {
#endif

void *my_malloc(unsigned long size);
int my_free(void *address);

#ifdef __cplusplus
}
#endif

#endif
