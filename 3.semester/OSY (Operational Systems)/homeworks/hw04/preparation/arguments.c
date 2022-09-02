#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define THREAD_ERROR -1

int primes[10] = {1, 2, 3, 5, 7, 11, 13, 17, 19, 23};

void *routine(void *arg) {
    int idx = *(int *)arg;
    printf("%d ", primes[idx]);
    free(arg);
    return NULL;
}

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    pthread_t threads[10];
    for (int i = 0; i < 10; ++i) {
        int * a = malloc(sizeof(int));
        *a = i;
        if ((pthread_create(threads + i, NULL, &(routine), a))) {
            fprintf(stderr, "Error: thread creation failed on line %d!\n", __LINE__);
            return THREAD_ERROR;
        }
    }
    for (int i = 0; i < 10; ++i) {
        if (pthread_join(threads[i], NULL)) {
            fprintf(stderr, "Error: thread join failed on line %d!\n", __LINE__);
            return THREAD_ERROR;
        }
    }
    printf("\n");
    return ret;
}
