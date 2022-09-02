#include "mem_alloc.h"
#include <stdio.h>

/*
 * Template for 11malloc. If you want to implement it in C++, rename
 * this file to mem_alloc.cc.
 */

static inline void *nbrk(void *address);

#ifdef NOVA

/**********************************/
/* nbrk() implementation for NOVA */
/**********************************/

static inline unsigned syscall2(unsigned w0, unsigned w1)
{
    asm volatile("   mov %%esp, %%ecx    ;"
                 "   mov $1f, %%edx      ;"
                 "   sysenter            ;"
                 "1:                     ;"
                 : "+a"(w0)
                 : "S"(w1)
                 : "ecx", "edx", "memory");
    return w0;
}

static void *nbrk(void *address)
{
    return (void *)syscall2(3, (unsigned)address);
}
#else

/***********************************/
/* nbrk() implementation for Linux */
/***********************************/

#include <unistd.h>

static void *nbrk(void *address)
{
    void *current_brk = sbrk(0);
    if (address != NULL) {
        int ret = brk(address);
        if (ret == -1)
            return NULL;
    }
    return current_brk;
}

#endif

void *my_malloc(unsigned long size)
{
    /* TODO: Implement in 11malloc */
    printf("my_malloc not implemented\n");
    return (void*)0;
}

int my_free(void *address)
{
    /* TODO: Implement in 11malloc */
    printf("my_free not implemented\n");
    return -1;
}
