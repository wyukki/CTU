#include "utils.h"

int find_string_in_array(const char **array, int length, char *what) {
    for (int i = 0; i < length; i++)
        if (strcmp(array[i], what) == 0)
            return i;
    return -1;
}

void initMutex(pthread_mutex_t *mutex) {
    assert(pthread_mutex_init(mutex, NULL) == 0);
}

void destroyMutex(pthread_mutex_t *mutex) {
    assert(pthread_mutex_destroy(mutex) == 0);
}

void initCondVar(pthread_cond_t *condvar) {
    assert(pthread_cond_init(condvar, NULL) == 0);
}

void destroyCondVar(pthread_cond_t *condvar) {
    assert(pthread_cond_destroy(condvar) == 0);
}

int getSleep(int workplace) {
    int sleep = 1000;
    switch (workplace) {
        case 0:
            sleep *= 100;
            break;
        case 1:
            sleep *= 200;
            break;
        case 2:
            sleep *= 150;
            break;
        case 3:
            sleep *= 300;
            break;
        case 4:
            sleep *= 400;
            break;
        case 5:
            sleep *= 250;
            break;
        case 6:
            sleep *= 500;
            break;
    }
    return sleep;
}
