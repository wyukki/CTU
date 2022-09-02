// #include <unistd.h> /* TODO: write your own system call wrappers */
// /*       for read, write, exit */
// #include <stdio.h>  /* TODO: sprintf -- convert number to hex string */
// #include <string.h> /* TODO: strlen -- length of output for write */

int isNum(char ch);
int isSpc(char ch);
static void print(unsigned num);

#define SYS_EXIT 1
#define STDIN 0
#define STDOUT 1
#define SYS_READ 3
#define SYS_WRITE 4
/* TODO: main() is called by libc. Real entry point is _start(). */
/**
 * Assignment: https://osy.pages.fel.cvut.cz/docs/cviceni/lab8/ (in czech) 
 */
int _start() {
    char buf[20];
    unsigned num = 0;
    int i;
    int numDigits = 0;
    unsigned charsInBuffer = 0;

    for (/* no init */; /* no end condition */; i++, charsInBuffer--) {
        if (charsInBuffer == 0) {
            int ret = 0;
            asm volatile("int $0x80"
                         : "=ret"(ret)
                         : "a"(SYS_READ), "b"(STDIN), "c"(buf), "d"(sizeof(buf))
                         : "memory");
            if (ret < 0) {
                asm ("int $0x80"
                             : 
                             : "a"(SYS_EXIT), "b"(1));
            }
            i = 0;
            charsInBuffer = ret;
        }
        if (numDigits > 0 && (charsInBuffer == 0 /* EOF */ ||
                              !isNum(buf[i]))) {
            print(num);
            numDigits = 0;
            num = 0;
        }
        if (charsInBuffer == 0 /* EOF */ ||
            (!isSpc(buf[i]) && !isNum(buf[i]))) {
            asm ("int $0x80"
                         :
                         : "a"(SYS_EXIT), "b"(0));
        }

        if (isNum(buf[i])) {
            num = num * 10 + buf[i] - '0';
            numDigits++;
        }
    }
}

int isNum(char ch) {
    return ch >= '0' && ch <= '9';
}

int isSpc(char ch) {
    return ch == ' ' || ch == '\n';
}

static void print(unsigned num) {
    char buf[20];
    int len = 0;
    if (!num) {
        int ret;
        asm volatile("int $0x80"
                     : "=a"(ret)
                     : "a"(SYS_WRITE), "b"(STDOUT), "c"("0x0"), "d"(sizeof("0x0"))
                     : "memory", "cc");
        if (ret == -1) {
            asm ("int $0x80"
                         :
                         : "a"(SYS_EXIT), "b"(1));
        }
        char newLineBuf[1];
        newLineBuf[0] = '\n';
        asm volatile("int $0x80"
                     : "=a"(ret)
                     : "a"(SYS_WRITE), "b"(STDOUT), "c"(newLineBuf), "d"(1)
                     : "memory", "cc");
        if (ret == -1) {
            asm ("int $0x80"
                         :
                         : "a"(SYS_EXIT), "b"(1));
        }
    } else {
        while (num) {
            int remainder = num % 16;
            char converted;
            if (remainder > 9) {
                switch (remainder) {
                    case 10:
                        converted = 'a';
                        break;
                    case 11:
                        converted = 'b';
                        break;
                    case 12:
                        converted = 'c';
                        break;
                    case 13:
                        converted = 'd';
                        break;
                    case 14:
                        converted = 'e';
                        break;
                    case 15:
                        converted = 'f';
                        break;
                }
            } else {
                converted = '0' + remainder;
            }
            buf[len++] = converted;
            num /= 16;
        }
        char output[len + 3];
        output[0] = '0';
        output[1] = 'x';
        for (int i = 0; i < len; ++i) {
            output[i + 2] = buf[len - i - 1];
        }
        int ret;
        asm volatile("int $0x80"
                     : "=a"(ret)
                     : "a"(SYS_WRITE), "b"(STDOUT), "c"(output), "d"(sizeof(output))
                     : "memory", "cc");
        if (ret == -1) {
            asm ("int $0x80"
                         :
                         : "a"(SYS_EXIT), "b"(1));
        }
        for (int i = 0; i < len + 2; ++i) {
            output[i] = '\0';
        }
        char newLineBuf[1];
        newLineBuf[0] = '\n';
        asm volatile("int $0x80"
                     : "=a"(ret)
                     : "a"(SYS_WRITE), "b"(STDOUT), "c"(newLineBuf), "d"(1)
                     : "memory", "cc");
        if (ret == -1) {
            asm ("int $0x80"
                         :
                         : "a"(SYS_EXIT), "b"(1));
        }
    }
}
