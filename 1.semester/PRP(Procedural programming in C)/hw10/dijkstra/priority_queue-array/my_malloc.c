#include <stdio.h>
#include <stdlib.h>

#include "my_malloc.h"

void* myMalloc(size_t size)
{
   void *ret = malloc(size);
   if (!ret) {
      fprintf(stderr, "Malloc failed!\n");
      exit(-1);
   }
   return ret;
}
