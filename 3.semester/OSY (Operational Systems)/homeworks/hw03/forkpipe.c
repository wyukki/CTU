#include <signal.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#define ERR_RETURN_VALUE -1
#define SYSCALL_ERR_RETURN_VALUE 2

volatile bool stopGEN = false;

void handler(int sig) {
    if (sig == SIGTERM) {
        stopGEN = true;
    }
}

/**
 * Implement next:
 * 1. Create two processes using fork syscall. First created process we will call
 * GEN, second - NSD. Main process (parent process) wee will call MAIN.
 * 2. Connect children with pipe (syscalls pipe and dup2) the way,
 * that stout of GEN will be connected to stdin of NSD.
 * 3. Wait five seconds with sleep(5) syscall
 * 4. Send to GEN SIGTERM signal, that will end both children.
 * 5. Wait untill children processes will finish
 * 6. If at least one of the child ends with an error, print to stdout message
 * "ERROR" and end with a return value 1, otherwise print "OK" and return 0.
 * 7. If some of the syscall will end with an error, main process should be
 * immediatly end with an return value 2.
 * 8. GEN will print to stdout two random numbers. GEN should wait 1 second between
 * each print.
 * 9. GEN should react to SIGTERM signal. When signal is recieved, GEN will print 
 * to stderr message "GEN TERMINATED" and return value 0.
 * 10. After NSD is created, it should call execl function and run nsd program.
 * 
 * Assignment: https://osy.pages.fel.cvut.cz/docs/cviceni/lab4/ (only in czech)
 */

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    int id1;
    int id2;
    int fd[2];
    if (pipe(fd)) {
        printf("ERROR\n");
        fflush(stdout);
        return SYSCALL_ERR_RETURN_VALUE;
    }
    if ((id1 = fork()) == ERR_RETURN_VALUE) {
        printf("ERROR\n");
        fflush(stdout);
        return SYSCALL_ERR_RETURN_VALUE;
    }
    if (id1 == 0) {
        if (close(fd[0]) == ERR_RETURN_VALUE) {
            return SYSCALL_ERR_RETURN_VALUE;
        }
        if (dup2(fd[1], STDOUT_FILENO) == ERR_RETURN_VALUE) {
            return SYSCALL_ERR_RETURN_VALUE;
        }
        if (close(fd[1]) == ERR_RETURN_VALUE) {
            return SYSCALL_ERR_RETURN_VALUE;
        }
        if (signal(SIGTERM, handler) == SIG_ERR) {
            return SYSCALL_ERR_RETURN_VALUE;
        }
        while (!stopGEN) {
            printf("%d %d\n", rand() % 4096, rand() % 4096);
            fflush(stdout);
            sleep(1);
        }
        fprintf(stderr, "GEN TERMINATED\n");
        return 0;
    } else {
        if ((id2 = fork()) == ERR_RETURN_VALUE) {
            printf("ERROR\n");
            fflush(stdout);
            return SYSCALL_ERR_RETURN_VALUE;
        }
        if (id2 == 0) {
            if (close(fd[1]) == ERR_RETURN_VALUE) {
                return SYSCALL_ERR_RETURN_VALUE;
            }
            if (dup2(fd[0], STDIN_FILENO) == ERR_RETURN_VALUE) {
                return SYSCALL_ERR_RETURN_VALUE;
            }
            if (close(fd[0]) == ERR_RETURN_VALUE) {
                return SYSCALL_ERR_RETURN_VALUE;
            }
            if (execl("nsd", "nsd", NULL) == ERR_RETURN_VALUE) {
                return SYSCALL_ERR_RETURN_VALUE;
            }
        } else {
            if (close(fd[0]) == ERR_RETURN_VALUE) {
                return SYSCALL_ERR_RETURN_VALUE;
            }
            if (close(fd[1]) == ERR_RETURN_VALUE) {
                return SYSCALL_ERR_RETURN_VALUE;
            }
            sleep(5);
            if (kill(id1, SIGTERM) == ERR_RETURN_VALUE) {
                return SYSCALL_ERR_RETURN_VALUE;
            }
        }
    }
    int wStatusFirst;
    if (waitpid(id1, &wStatusFirst, 0) == ERR_RETURN_VALUE) {
        printf("ERROR\n");
        return SYSCALL_ERR_RETURN_VALUE;
    }
    int retValue1 = WEXITSTATUS(wStatusFirst);
    int wStatusSecond;
    if (waitpid(id2, &wStatusSecond, 0) == ERR_RETURN_VALUE) {
        printf("ERROR\n");
        return SYSCALL_ERR_RETURN_VALUE;
    }
    int retValue2 = WEXITSTATUS(wStatusSecond);
    if (retValue1 || retValue2) {
        printf("ERROR\n");
        ret = EXIT_FAILURE;
    } else {
        printf("OK\n");
    }
    return ret;
}
