#include "linkedList.h"
#include "products.h"
#include "queue.h"
#include "utils.h"

pthread_mutex_t mutexPlaces;
pthread_mutex_t workMutex;
pthread_mutex_t productMutex;
pthread_mutex_t stopMutex;
pthread_mutex_t EOFMutex;
pthread_mutex_t mustFinishMutex;

pthread_cond_t workCond;
pthread_cond_t stopFactoryCond;
queue_t *scissors;
queue_t *drills;
queue_t *benders;
queue_t *welders;
queue_t *paints;
queue_t *screwdrievers;
queue_t *millers;
ll_t *linkedList;

void setQueueToNULL(int workspace);

void *work(void *arg);
char lastPiece(product_t *product, int workplace);
queue_t *findQueue(int workplace);
void pushToAllQueues(product_t *product);
void stopFactory();
bool stopQueue();
void freeStructures();
bool canHaveASemiProduct(queue_t *queue, int workplace);
int ready_places[_PLACE_COUNT];
bool EOFReached = false;
bool checkAgain = false;

int main(int argc, char **argv) {
    for (int i = 0; i < _PRODUCT_COUNT; ++i) {
        ready_places[i] = 0;
    }
    linkedList = initList();
    scissors = initQueue();
    drills = initQueue();
    benders = initQueue();
    welders = initQueue();
    paints = initQueue();
    screwdrievers = initQueue();
    millers = initQueue();
    if (!linkedList || !scissors || !drills || !benders ||
        !welders || !paints || !screwdrievers || !millers) {
        return 1;
    }
    initMutex(&mutexPlaces);
    initMutex(&EOFMutex);
    initMutex(&mustFinishMutex);
    initMutex(&workMutex);
    initMutex(&stopMutex);
    initMutex(&productMutex);
    initCondVar(&workCond);
    initCondVar(&stopFactoryCond);
    int numberOfReadLines = 0;
    while (1) {
        numberOfReadLines++;
        char *line, *cmd, *arg1, *arg2, *arg3, *saveptr;
        int s = scanf(" %m[^\n]", &line);
        if (s == EOF)
            break;
        if (s == 0)
            continue;

        cmd = strtok_r(line, " ", &saveptr);
        arg1 = strtok_r(NULL, " ", &saveptr);
        arg2 = strtok_r(NULL, " ", &saveptr);
        arg3 = strtok_r(NULL, " ", &saveptr);

        if (strcmp(cmd, "start") == 0 && arg1 && arg2 && !arg3) {
            int counterOfWrongInput = 0;
            if (strcmp(arg2, "nuzky")) {
                counterOfWrongInput++;
            }
            if (strcmp(arg2, "vrtacka")) {
                counterOfWrongInput++;
            }
            if (strcmp(arg2, "ohybacka")) {
                counterOfWrongInput++;
            }
            if (strcmp(arg2, "svarecka")) {
                counterOfWrongInput++;
            }
            if (strcmp(arg2, "lakovna")) {
                counterOfWrongInput++;
            }
            if (strcmp(arg2, "sroubovak")) {
                counterOfWrongInput++;
            }
            if (strcmp(arg2, "freza")) {
                counterOfWrongInput++;
            }
            if (counterOfWrongInput == 7) {
                fprintf(stderr, "Error: wrong worplace!\n");
            } else {
                char *name = (char *)malloc(strlen(arg1) + 1);
                if (!name) {
                    fprintf(stderr, "Allocation error on line %d in file %s\n", __LINE__, __FILE__);
                    return 1;
                }
                int workspace = find_string_in_array(place_str, _PLACE_COUNT, arg2);
                strcpy(name, arg1);
                name[strlen(name)] = '\0';
                ll_entry_t *newEntry = allocateEntry(name, workspace);
                push(linkedList, newEntry);

                // start thread
                // send workspace as an argument
                if (pthread_create(&newEntry->thread, NULL, work, (void *)name)) {
                    fprintf(stderr, "Error: creating thread!\n");
                    exit(-1);
                }
            }
        } else if (strcmp(cmd, "make") == 0 && arg1 && !arg2) {
            int type = find_string_in_array(
                product_str,
                _PRODUCT_COUNT,
                arg1);
            if (type >= 0) {
                product_t *product = createProduct(type);
                product->productType = arg1[0];
                pushToAllQueues(product);
                pthread_cond_broadcast(&workCond);
            }

        } else if (strcmp(cmd, "end") == 0 && arg1 && !arg2) {
            ll_entry_t *curr = linkedList->head;
            while (curr) {
                if (!strcmp(curr->name, arg1)) {
                    pthread_mutex_lock(&mustFinishMutex);
                    curr->mustFinish = true;
                    pthread_mutex_unlock(&mustFinishMutex);
                    pthread_cond_broadcast(&workCond);
                    break;
                }
                curr = curr->next;
            }
        } else if (strcmp(cmd, "add") == 0 && arg1 && !arg2) {
            int index = find_string_in_array(place_str, _PLACE_COUNT, arg1);
            if (index >= 0) {
                pthread_mutex_lock(&mutexPlaces);
                ready_places[index]++;
                pthread_mutex_unlock(&mutexPlaces);
                pthread_cond_broadcast(&workCond);
            } else {
                fprintf(stderr, "Error: wrong workplace!\n");
            }
        } else if (strcmp(cmd, "remove") == 0 && arg1 && !arg2) {
            int index = find_string_in_array(place_str, _PLACE_COUNT, arg1);
            if (index >= 0) {
                pthread_mutex_lock(&mutexPlaces);
                if (ready_places[index] > 0) {
                    ready_places[index]--;
                } else {
                    fprintf(stderr, "Cannot remove workplace no. %d!\n", index);
                }
                pthread_mutex_unlock(&mutexPlaces);
            }
        } else {
            fprintf(stderr, "Invalid command: %s\n", line);
        }
        free(line);
    }
    pthread_mutex_lock(&EOFMutex);
    EOFReached = true;
    pthread_mutex_unlock(&EOFMutex);
    stopFactory();
    destroyMutex(&mutexPlaces);
    destroyMutex(&EOFMutex);
    destroyMutex(&mustFinishMutex);
    destroyMutex(&stopMutex);
    destroyMutex(&workMutex);
    destroyMutex(&productMutex);
    destroyCondVar(&workCond);
    destroyCondVar(&stopFactoryCond);
    freeStructures();
}

void *work(void *arg) {
    char *name = (char *)arg;
    int workplace = 0;
    ll_entry_t *worker = linkedList->head;

    while (worker) {
        if (!strcmp(worker->name, name)) {
            workplace = worker->workspace;
            break;
        }
        worker = worker->next;
    }
    queue_t *myQueue = findQueue(workplace);

    while (1) {
        queue_entry_t *currEntry;
        pthread_mutex_lock(&workMutex);
        while (ready_places[workplace] <= 0 || !(currEntry = hasRequest(myQueue, workplace)) ||
               worker->mustFinish) {
            pthread_mutex_lock(&mustFinishMutex);
            if (worker->mustFinish) {
                pthread_mutex_unlock(&mustFinishMutex);
                pthread_mutex_unlock(&workMutex);
                return NULL;
            }
            pthread_mutex_unlock(&mustFinishMutex);
            pthread_cond_wait(&workCond, &workMutex);
        }
        pthread_mutex_unlock(&workMutex);
        product_t *currProduct = currEntry->product;
        pthread_mutex_lock(&mutexPlaces);
        ready_places[workplace]--;
        pthread_mutex_unlock(&mutexPlaces);
        // do work
        int sleep = getSleep(workplace);
        printf("%s %s %d %c\n", name, place_str[workplace], currProduct->step + 1, currProduct->productType);
        char c = lastPiece(currProduct, workplace);
        if (c) {
            printf("done %c\n", c);
            pthread_mutex_lock(&productMutex);
            free(currEntry->product);
            currEntry->product = NULL;
            pthread_mutex_unlock(&productMutex);
        } else {
            currEntry->product->step++;
        }
        usleep(sleep);
        pthread_mutex_lock(&workMutex);
        popFromQueue(myQueue);
        ready_places[workplace]++;
        pthread_mutex_unlock(&workMutex);
        pthread_cond_broadcast(&workCond);
        pthread_mutex_lock(&EOFMutex);
        if (EOFReached) {
            pthread_cond_broadcast(&stopFactoryCond);
            checkAgain = true;
        }
        pthread_mutex_unlock(&EOFMutex);
    }
    return NULL;
}

char lastPiece(product_t *product, int workplace) {
    int step = product->step;
    if (product->steps[step] == workplace && step == 5) {
        return product->productType;
    }
    return '\0';
}

queue_t *findQueue(int workplace) {
    queue_t *queue;
    switch (workplace) {
        case 0:
            queue = scissors;
            break;
        case 1:
            queue = drills;
            break;
        case 2:
            queue = benders;
            break;
        case 3:
            queue = welders;
            break;
        case 4:
            queue = paints;
            break;
        case 5:
            queue = screwdrievers;
            break;
        case 6:
            queue = millers;
            break;
        default:
            queue = NULL;
            break;
    }
    return queue;
}

void pushToAllQueues(product_t *product) {
    switch (product->productType) {
        case 'A':
            pushToQueue(scissors, product);
            pushToQueue(drills, product);
            pushToQueue(benders, product);
            pushToQueue(welders, product);
            pushToQueue(drills, product);
            pushToQueue(paints, product);
            break;
        case 'B':
            pushToQueue(drills, product);
            pushToQueue(scissors, product);
            pushToQueue(millers, product);
            pushToQueue(drills, product);
            pushToQueue(paints, product);
            pushToQueue(screwdrievers, product);
            break;
        case 'C':
            pushToQueue(millers, product);
            pushToQueue(drills, product);
            pushToQueue(screwdrievers, product);
            pushToQueue(drills, product);
            pushToQueue(millers, product);
            pushToQueue(paints, product);
            break;
    }
}
void stopFactory() {
    pthread_mutex_lock(&stopMutex);
    bool allWorkplacesFinished;
    while (!(allWorkplacesFinished = stopQueue())) {
        if (checkAgain) {
            checkAgain = false;
            continue;
        }
        pthread_cond_wait(&stopFactoryCond, &stopMutex);
    }
    pthread_mutex_unlock(&stopMutex);
}

bool stopQueue() {
    bool ret = true;
    for (int i = NUZKY; i <= FREZA; ++i) {
        if (!linkedList->size) {  // all workers are freed
            break;
        }
        queue_t *queueType = findQueue(i);
        if (!queueType) {
            // printf("The queue no. %d has been already freed!\n", i);
            continue;
        }
        if (queueIsEmpty(queueType)) {
            fprintf(stderr, "Terminating %d process because queue is empty!\n", i);
            freeWorkers(linkedList, &mustFinishMutex, i, &workCond);
            freeQueue(queueType);
            setQueueToNULL(i);
        } else if (!canHaveASemiProduct(queueType, i)) {
            fprintf(stderr, "Terminating %d process because this worker can't have a semi-product!\n", i);
            freeWorkers(linkedList, &mustFinishMutex, i, &workCond);
            freeQueue(queueType);
            setQueueToNULL(i);
        } else if (!find(linkedList, i)) {
            fprintf(stderr, "Terminating %d process because there is not worker for this process!\n", i);
            freeWorkers(linkedList, &mustFinishMutex, i, &workCond);
            freeQueue(queueType);
            setQueueToNULL(i);
        } else {
            // fprintf(stderr, "Can't finish process %d\n", i);
            ret = false;
        }
    }
    return ret;
}
void freeStructures() {
    freeList(linkedList);
}

void setQueueToNULL(int workspace) {
    switch (workspace) {
        case 0:
            scissors = NULL;
            break;
        case 1:
            drills = NULL;
            break;
        case 2:
            benders = NULL;
            break;
        case 3:
            welders = NULL;
            break;
        case 4:
            paints = NULL;
            break;
        case 5:
            screwdrievers = NULL;
            break;
        case 6:
            millers = NULL;
            break;
    }
}
bool canHaveASemiProduct(queue_t *queue, int workplace) {
    if (!queue) {
        return true;
    }
    bool ret = true;
    queue_entry_t *curr = queue->head;
    while (curr) {
        pthread_mutex_lock(&productMutex);
        if (!curr->product) {
            pthread_mutex_unlock(&productMutex);
            curr = curr->next;
            continue;
        }
        product_t *currProduct = curr->product;
        switch (currProduct->productType) {
            case 'A':
                if (!currProduct->step && !findQueue(productA[0])) {
                    ret = false;
                } else {
                    for (int i = currProduct->step; i >= 1; --i) {
                        if (!findQueue(productA[i])) {
                            ret = false;
                            break;
                        }
                    }
                }
                break;
            case 'B':
                if (!currProduct->step && !findQueue(productB[0])) {
                    ret = false;
                } else {
                    for (int i = currProduct->step; i >= 1; --i) {
                        if (!findQueue(productB[i])) {
                            ret = false;
                            break;
                        }
                    }
                }
                break;
            case 'C':
                if (!currProduct->step && !findQueue(productB[0])) {
                    ret = false;
                } else {
                    for (int i = currProduct->step; i >= 1; --i) {
                        if (!findQueue(productC[i])) {
                            ret = false;
                            break;
                        }
                    }
                }
                break;
        }
        pthread_mutex_unlock(&productMutex);
        if (!ret) {
            break;
        }
        curr = curr->next;
    }
    return ret;
}
