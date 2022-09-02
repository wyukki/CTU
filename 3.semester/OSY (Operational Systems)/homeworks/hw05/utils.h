#ifndef _UTILS_H
#define _UTILS_H
#include <assert.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int find_string_in_array(const char **array, int length, char *what);
void initMutex(pthread_mutex_t *mutex);
void destroyMutex(pthread_mutex_t *mutex);
void initCondVar(pthread_cond_t *condvar);
void destroyCondVar(pthread_cond_t *condvar);
int getSleep(int workplace);

#endif
