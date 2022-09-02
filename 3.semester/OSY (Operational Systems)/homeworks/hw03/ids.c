#define _POSIX_C_SOURCE 200112L
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdbool.h>

#define ERR_RETURN_VALUE -1
#define SYSCALL_ERR_RETURN_VALUE 2

volatile bool stopGEN = 0;

void handler(int sig) {
    if (sig == SIGTERM) {
        stopGEN = true;
    }
}

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    int id1;
    if ((id1 = fork()) == ERR_RETURN_VALUE) {
        fprintf(stderr, "An error occured with fork");
        return SYSCALL_ERR_RETURN_VALUE;
    }
    if (id1 == 0) {
        while (!stopGEN) {
            fprintf(fd[1],"%d %d\n", rand() % 4096, rand() % 4096);
            if (signal(SIGTERM, handler) == SIG_ERR) {
                printf("Cannot catch a SIGTERM!\n");
                fflush();
                return SYSCALL_ERR_RETURN_VALUE;
       	    }
            printf("GEN TERMINATED!\n");
	    return 0;
	}
    } else {
        int id2;
        if ((id2 = fork()) == ERR_RETURN_VALUE) {
            fprintf(stderr, "An error occured with fork");
            return SYSCALL_ERR_RETURN_VALUE;
        }
        if (id2 == 0) {
            printf("Second child\n");
        } else {
            sleep(5);
            printf("killing firstchild %d\n", getpid() + 1);
            kill(getpid() + 1, SIGTERM);
        }
    }
    if (id1) {
        printf("CLEN\n");
    }
    ret = wait(NULL);
    ret = wait(NULL);
    return ret;
}
