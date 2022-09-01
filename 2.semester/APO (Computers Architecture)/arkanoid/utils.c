#include "utils.h"

void *myMalloc(int size) {
    void *memBlock = malloc(size);
    if (!memBlock) {
        fprintf(stderr, "Allocation error!\n");
        return NULL;
    }
    return memBlock;
}
void setTerminalNonBlock(bool gameFinished) {
    static struct termios standardTerminal, terminalNonBlock;
    tcgetattr(STDIN_FILENO, &terminalNonBlock);
    if (!gameFinished) {
        standardTerminal = terminalNonBlock;
        terminalNonBlock.c_iflag &= ~(IGNBRK | BRKINT | PARMRK | ISTRIP | INLCR | ICRNL | IXON | IXOFF);
        terminalNonBlock.c_lflag &= ~(ECHO | ECHONL | ICANON | ISIG);
        terminalNonBlock.c_cflag &= ~(CSIZE | PARENB);
        terminalNonBlock.c_cflag |= CS8;
    } else {
        terminalNonBlock = standardTerminal;
    }
    tcsetattr(STDIN_FILENO, TCSANOW, &terminalNonBlock);
}

void setCursorPosition(int x, int y) {
    printf("\033[%d;%dH", y, x);
}

void setDisplayBlack(unsigned char *parlcd_mem_base, unsigned short *fb) {
    parlcd_write_cmd(parlcd_mem_base, 0x2c);
    for (int i = 0; i < DISPLAY_HEIGHT * DISPLAY_WIDTH; ++i) {
        fb[i] = BLACK_COLOR;
        parlcd_write_data(parlcd_mem_base, fb[i]);
    }
}

void finishGame(bool gameFinished, unsigned char *parlcd_mem_base, unsigned short *fb) {
    if (gameFinished) {
        setTerminalNonBlock(gameFinished);
        setDisplayBlack(parlcd_mem_base, fb);
    } else {
        return;
    }
}

void freeAllObjects(char **map, platform_t *platform, ball_t *ball, unsigned short *fb) {
    for (int i = 0; i < MAP_HEIGHT; ++i) {
        free(map[i]);
    }
    free(map);
    free(platform);
    free(ball);
    free(fb);
}
