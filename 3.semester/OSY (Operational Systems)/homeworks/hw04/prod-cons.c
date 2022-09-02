#include "utils.h"

ll_t *linkedList;
sem_t semEmpty;
pthread_mutex_t mutexList;
pthread_mutex_t mutexProducerDone;
bool producerDone = false;
// takes the number of thread as a parametr
void *consumer(void *arg);
// returns 0 if input was correct, 1 otherwise
void *producer(void *arg);
/**
 * Assignment: https://osy.pages.fel.cvut.cz/docs/cviceni/lab5/ (in czech)
 */
int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    int CPUs = sysconf(_SC_NPROCESSORS_ONLN);
    int *producersReturnValue;
    int consumerNumber = argc > 1 ? (int)strtol((argv[1]), NULL, 10) : 1;
    if (consumerNumber <= 0) {
        fprintf(stderr, "Error: the number of consumers must be greater than 0!\n");
        return RUNTIME_ERROR;
    } else if (consumerNumber > CPUs) {
        fprintf(stderr, "Error: the number of consumers must be less than CPU's number!\n");
        return RUNTIME_ERROR;
    }
    linkedList = initList();
    if (!linkedList) {
        return RUNTIME_ERROR;
    }
    pthread_t producent;
    pthread_mutex_init(&mutexList, NULL);
    pthread_mutex_init(&mutexProducerDone, NULL);
    sem_init(&semEmpty, 0, 0);
    pthread_t conzument[consumerNumber];
    if (pthread_create(&producent, NULL, &(producer), NULL)) {
        fprintf(stderr, "Error with creating producer!\n");
        ret = RUNTIME_ERROR;
    }
    for (int i = 0; i < consumerNumber; ++i) {
        int *threadNumber = malloc(sizeof(int));
        *threadNumber = i + 1;
        if (pthread_create(conzument + i, NULL, &(consumer), threadNumber)) {
            fprintf(stderr, "Error with creating %d'th consemer!\n", i);
            ret = RUNTIME_ERROR;
        }
    }
    if (pthread_join(producent, (void **)&producersReturnValue)) {
        fprintf(stderr, "Error with joining a producer!\n");
        ret = RUNTIME_ERROR;
    }
    if (*producersReturnValue) {
        ret = RUNTIME_ERROR;
    }
    for (int i = 0; i < consumerNumber; ++i) {
        sem_post(&semEmpty);
    }
    for (int i = 0; i < consumerNumber; ++i) {
        if (pthread_join(conzument[i], NULL)) {
            fprintf(stderr, "Error with joining %d'th consemer!\n", i);
            ret = RUNTIME_ERROR;
        }
    }
    freeList(linkedList);
    free(producersReturnValue);
    pthread_mutex_destroy(&mutexList);
    pthread_mutex_destroy(&mutexProducerDone);
    sem_destroy(&semEmpty);
    return ret;
}

void *consumer(void *arg) {
    int threadNumber = *(int *)arg;
    bool mustFinish = false;
    while (!mustFinish) {
        sem_wait(&semEmpty);
        pthread_mutex_lock(&mutexList);
        entry_t *head = pop(linkedList);
        pthread_mutex_unlock(&mutexList);
        if (!head) {
            free(arg);
            return NULL;
        }
        pthread_mutex_lock(&mutexProducerDone);
        mustFinish = producerDone && !head;
        pthread_mutex_unlock(&mutexProducerDone);
        char *msg = malloc(strlen(head->text) * head->number + head->number + strlen("Thread : ") + 3); // + 3 stands for
                                                                                                        // '\0' char, and for thread number
        if (!msg) {
            printf("Thread %d:", threadNumber);
            for (int i = 0; i < head->number; ++i) {
                printf("%s", head->text);
                if (i != head->number - 1) {
                    printf(" ");
                }
            }
            printf("\n");
        } else {
            sprintf(msg, "Thread %d:", threadNumber);
            for (int i = 0; i < head->number; ++i) {
                strcat(msg, " ");
                strcat(msg, head->text);
            }
            printf("%s\n", msg);
        }
        free(head->text);
        free(head);
        free(msg);
    }
    free(arg);
    return NULL;
}

void *producer(void *arg) {
    int *retValue = malloc(sizeof(int));
    int ret, number;
    char *text = NULL;
    while ((ret = scanf("%d %ms", &number, &text)) == 2) {
        if (number < 0) {
            ret = 0;
            break;
        }
        pthread_mutex_lock(&mutexList);
        push(linkedList, number, text);
        pthread_mutex_unlock(&mutexList);
        sem_post(&semEmpty);
    }
    if (ret != 2 && ret != EOF) {
        fprintf(stderr, "Error: wrong input!\n");
        *retValue = 1;
        producerDone = true;
        return (void *)retValue;
    }
    *retValue = 0;
    pthread_mutex_lock(&mutexProducerDone);
    producerDone = true;
    pthread_mutex_unlock(&mutexProducerDone);
    return (void *)retValue;
}
