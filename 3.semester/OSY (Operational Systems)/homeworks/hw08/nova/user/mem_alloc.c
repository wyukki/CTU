#include "mem_alloc.h"

#include <stdbool.h>
#include <stddef.h>
#include <stdio.h>
#define ALLOCATION_ERROR 100
/*
 * Template for 11malloc. If you want to implement it in C++, rename
 * this file to mem_alloc.cc.
 */

static inline void *nbrk(void *address);

// #ifdef NOVA

/**********************************/
/* nbrk() implementation for NOVA */
/**********************************/

static inline unsigned syscall2(unsigned w0, unsigned w1) {
    asm volatile(
        "   mov %%esp, %%ecx    ;"
        "   mov $1f, %%edx      ;"
        "   sysenter            ;"
        "1:                     ;"
        : "+a"(w0)
        : "S"(w1)
        : "ecx", "edx", "memory");
    return w0;
}

static void *nbrk(void *address) {
    return (void *)syscall2(3, (unsigned)address);
}

// #else
/***********************************/
/* nbrk() implementation for Linux */
/***********************************/

// #include <unistd.h>

// static void *nbrk(void *address) {
//     void *current_brk = sbrk(0);
//     if (address != NULL) {
//         int ret = brk(address);
//         if (ret == -1)
//             return NULL;
//     }
//     return current_brk;
// }

// #endif

void *break_min = NULL;

typedef struct entry {
    unsigned long block_size;
    bool is_free;
    struct entry *next;
} entry_t;

unsigned long allocated_mem = 1;
entry_t *head = NULL;
entry_t *tail = NULL;

entry_t *find_free_space(unsigned long int);
entry_t *create_entry(void *addr, unsigned long block_size);

void *my_malloc(unsigned long size) {
    if (!break_min) {
        break_min = nbrk((void *)0x0);
    }
    if (!size) {
        return NULL;
    }
    void *addr;
    if (!head) {  // first allocation
        if (!(addr = nbrk(break_min + size + sizeof(entry_t)))) {
            return NULL;
        }
        entry_t *new_entry = create_entry(addr, size);
        head = new_entry;
        tail = new_entry;
        allocated_mem += size + sizeof(entry_t);
        return break_min + sizeof(entry_t);
    } else {
        entry_t *new_entry;
        if ((new_entry = find_free_space(size))) {
            new_entry->is_free = false;
            return (void *) new_entry + sizeof(entry_t);
        }
        // Allocate new block
        if (!(addr = nbrk(break_min + allocated_mem + size + (sizeof(entry_t))))) {
            return NULL;
        }
        new_entry = create_entry(addr, size);
        tail->next = new_entry;
        tail = new_entry;
        allocated_mem += size + sizeof(entry_t);
    }
    return addr + sizeof(entry_t);
}

int my_free(void *address) {
    if (address > nbrk((void *)0x0)) {
        return 1;
    } else if (address < break_min) {
        return 2;
    }
    int ret = 1;
    entry_t *curr = head;
    while (curr) {
        if (((void *)curr + sizeof(entry_t) == address && !curr->is_free)) {  // found
            curr->is_free = true;
            ret = 0;
            break;
        }
        curr = curr->next;
    }
    return ret;
}

entry_t *find_free_space(unsigned long size) {
    entry_t *curr = head;
    entry_t *prev = NULL;
    while (curr) {
        if (curr->is_free && curr->block_size >= size) {
            return curr;
        }
        if (prev && prev->is_free && curr->is_free) {
            prev->block_size += curr->block_size;
            prev->next = curr->next;
            if (prev->block_size >= size) {
                return prev;
            }
        } else {
            prev = curr;
        }
        curr = curr->next;
    }
    return NULL;
}

entry_t *create_entry(void *addr, unsigned long size) {
    entry_t *new_entry = (entry_t *)addr;
    new_entry->is_free = false;
    new_entry->block_size = size;
    new_entry->next = NULL;
    return new_entry;
}
