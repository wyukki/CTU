#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define THREAD_ERROR -1

void *rollDice() {
    int value = (rand() % 6) + 1;
    // printf("%d\n", value);
    int *result = malloc(sizeof(int));
    *result = value;
    return (void *) result;
}

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    int *res;
    pthread_t thread;
    srand(time(NULL));
    if ((pthread_create(&thread, NULL, &(rollDice), NULL))) {
        fprintf(stderr, "Error: thread creation failed on line %d!\n", __LINE__);
        return THREAD_ERROR;
    }
    if (pthread_join(thread, (void **) &res)) {
        fprintf(stderr, "Error: thread join failed on line %d!\n", __LINE__);
        return THREAD_ERROR;
    }
    printf("%d\n", *res);
    free(res);
    return ret;
}
