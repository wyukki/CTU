#include "mem_alloc.h"

int main()
{
    void *addr;

    addr = my_malloc(1024);
    my_free(addr);
    return 0;
}
