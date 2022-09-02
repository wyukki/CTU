#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

#define THREAD_ERROR -1

int mails = 0;
pthread_mutex_t mutex;

void *routine() {
    for (int i = 0; i < 10000000; ++i) {
        pthread_mutex_lock(&mutex);
        mails++;
        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    // pthread_t t1, t2;
    // thread array:
    pthread_t threads[4];
    pthread_mutex_init(&mutex, NULL);
    for (int i = 0; i < 4; ++i) {
        if ((pthread_create(threads + i, NULL, &(routine), NULL))) {
            fprintf(stderr, "Error: thread creation failed on line %d!\n", __LINE__);
            return THREAD_ERROR;
        }
        printf("Thread %d has started\n", i);
    }
    for (int i = 0; i < 4; ++i) {
        if (pthread_join(threads[i], NULL)) {
            fprintf(stderr, "Error: thread join failed on line %d!\n", __LINE__);
            return THREAD_ERROR;
        }
        printf("Thread %d has finished\n", i);
    }
    // if ((pthread_create(&t1, NULL, &(routine), NULL))) {
    //     fprintf(stderr, "Error: thread creation failed on line %d!\n", __LINE__);
    //     return THREAD_ERROR;
    // }
    // if ((pthread_create(&t2, NULL, &(routine), NULL))) {
    //     fprintf(stderr, "Error: thread creation failed on line %d!\n", __LINE__);
    //     return THREAD_ERROR;
    // }
    // if (pthread_join(t1, NULL)) {
    //     fprintf(stderr, "Error: thread join failed on line %d!\n", __LINE__);
    //     return THREAD_ERROR;
    // }
    // if (pthread_join(t2, NULL)) {
    //     fprintf(stderr, "Error: thread join failed on line %d!\n", __LINE__);
    //     return THREAD_ERROR;
    // }
    pthread_mutex_destroy(&mutex);
    printf("Number of mails = %d\n", mails);
    return ret;
}
