#include "objects.h"
#include "utils.h"
#define CHAR_HEIGHT 16
#define CHAR_WIDTH 8
#define ALLOC_ERROR -1
#define GAME_FINISHED 300
#define MAX_BLOCKS_ON_ROW 6
#define PI 3.14159265358979323846

void *getInput();
void printMap();
void *blinkLED();
void drawChar(int x, int y, char c);
void drawPixel(int x, int y, int color);
void drawPixelBig(int x, int y, int scale, int color);
void freeBlocks();
void putBallOnMap(int prevX, int prevY);
void initLevel();
void clearBlock(int x, int y);
void initBlock(int y, int x, int count);
void push(int xFrom, int xTo, int yFrom, int yTo);
void moveBall();
int restartLevel();

static volatile bool gameFinished = false;
static volatile bool gameStarted = false;
static ball_t *ball;
static platform_t *platform;
static blocks_t *blocks;
static char **map;
static volatile int level = 1;
static int lives = 3;
static int blocksCount = 0;
unsigned char *parlcd_mem_base;
unsigned char *led_mem_base;
unsigned short int *fb;

pthread_t input_thread;
pthread_t led_thread;
font_descriptor_t *fdes;

int main(int argc, char *argv[]) {
    int r = EXIT_SUCCESS;
    parlcd_mem_base = map_phys_address(PARLCD_REG_BASE_PHYS, PARLCD_REG_SIZE, 0);
    led_mem_base = map_phys_address(SPILED_REG_BASE_PHYS, SPILED_REG_SIZE, 0);
    fb = myMalloc(DISPLAY_WIDTH * DISPLAY_HEIGHT * 2);
    if (!parlcd_mem_base || !fb || !led_mem_base) {
        fprintf(stderr, "Maping error!\n");
        setTerminalNonBlock(true);
        return -1;
    }
    fdes = &font_winFreeSystem14x16;
    parlcd_hx8357_init(parlcd_mem_base);
    setDisplayBlack(parlcd_mem_base, fb);
    *(volatile uint32_t *)(led_mem_base + SPILED_REG_LED_RGB1_o) = BLACK_COLOR;
    *(volatile uint32_t *)(led_mem_base + SPILED_REG_LED_RGB2_o) = BLACK_COLOR;
    setTerminalNonBlock(gameFinished);

    if (!(map = initMap())) {
        fprintf(stderr, "Error occured while creating map!\n");
        return ALLOC_ERROR;
    } else if (!(platform = initPlatform(map))) {
        fprintf(stderr, "Error occured while creating platform!\n");
        return ALLOC_ERROR;
    } else if (!(ball = initBall(map, platform))) {
        fprintf(stderr, "Error occured while creating ball!\n");
        return ALLOC_ERROR;
    }
    initLevel();
    printMap();
    if ((pthread_create(&input_thread, NULL, *(getInput), NULL)) < 0) {
        fprintf(stderr, "Error: creating thread error!\n");
        return ALLOC_ERROR;
    } else if ((pthread_create(&led_thread, NULL, *(blinkLED), NULL)) < 0) {
        fprintf(stderr, "Error : creating thread error!\n");
        return ALLOC_ERROR;
    }

    if (pthread_join(led_thread, NULL)) {
        return ALLOC_ERROR;
    }
    while (!gameFinished) {
        if (gameStarted) {
            moveBall();
        }
    }
    finishGame(gameFinished, parlcd_mem_base, fb);
    freeAllObjects(map, platform, ball, fb);
    if (blocks) {
        freeBlocks();
    }
    if (pthread_join(input_thread, NULL)) {
        r = EXIT_FAILURE;
    }
    return r;
}

void *getInput() {
    int r;
    while (!gameFinished) {
        if ((r = getchar()) == EOF) {
            gameFinished = true;
            break;
        }
        if (r == '[') {
            fprintf(stderr, "Please use WASD keys!\n");
            continue;
        }
        if (gameFinished) {
            return NULL;
        }
        if (toupper(r) != 'Q' && !gameStarted) {
            printMap();
        }
        r = toupper(r);
        switch (r) {
            case 'A':
                if (/*map[platform->y][platform->xFrom] == '#' ||*/ map[platform->y][platform->xFrom - 1] == '#') {
                    continue;
                }
                platform->xFrom--;
                if (!gameStarted) {
                    map[ball->y][ball->x] = ' ';
                    ball->x--;
                    ball->xDir--;
                    map[ball->y][ball->x] = ball->sign;
                }
                map[platform->y][platform->xFrom] = platform->sign;
                map[platform->y][platform->xTo] = ' ';
                platform->xTo--;
                break;
            case 'D':
                if (/*map[platform->y][platform->xTo] == '#' ||*/ map[platform->y][platform->xTo + 1] == '#') {
                    continue;
                }
                if (!gameStarted) {
                    map[ball->y][ball->x] = ' ';
                    ball->x++;
                    ball->xDir++;
                    map[ball->y][ball->x] = ball->sign;
                }
                map[platform->y][platform->xFrom] = ' ';
                platform->xFrom++;
                platform->xTo++;
                map[platform->y][platform->xTo] = platform->sign;
                break;
            case 'W':
                break;
            case 'S':
                break;
            case 'Q':
                gameFinished = true;
                gameStarted = false;
                break;
            case ' ':
                gameStarted = true;
                break;
        }
    }
    return NULL;
}
void moveBall() {
    if (!ball || gameFinished || !gameStarted) {
        return;
    }
    if (ball->angle < 0) {
        ball->angle += 2 * PI;
    } else if (ball->angle > 2 * PI) {
        ball->angle -= 2 * PI;
    }
    ball_t prevState = {ball->x, ball->y, ball->sign, ball->xDir, ball->yDir, ball->velocity, ball->angle};

    ball->yDir = ball->yDir + sin(ball->angle) * ball->velocity;
    ball->xDir = ball->xDir + cos(ball->angle) * ball->velocity;
    ball->y = (int)round(ball->yDir);
    ball->x = (int)round(ball->xDir);
    if (ball->x == MAP_WIDTH - 1 && ball->y == 0) {
        ball->x = MAP_WIDTH - 2;
        ball->xDir = MAP_WIDTH - 2;
    }
    if (ball->y == MAP_HEIGHT - 1) {
        map[prevState.y][prevState.x] = ' ';
        lives--;
        if (lives == 0) {
            gameFinished = true;
	    int yi = MAP_HEIGHT / 2 - 5;
	    char *msg = "GAME OVER";
	    int len = strlen(msg);
	    int xi = MAP_WIDTH / 2 - len;
	    for (int k = 0; k < len; ++k) {
		map[yi][xi] = msg[k];
		xi++;
	    }
            printMap();
            printf("GAME OVER! PRESS ENTER TO EXIT!\n");
        } else {
            if ((pthread_create(&led_thread, NULL, *(blinkLED), NULL)) < 0) {
                fprintf(stderr, "Error : creating thread error!\n");
            }
            if (pthread_join(led_thread, NULL)) {
                exit(ALLOC_ERROR);
            }
        }

        if (restartLevel() < 0) {
            setTerminalNonBlock(true);
            free(platform);
            free(ball);
            for (int i = 0; i < MAP_HEIGHT; ++i) {
                free(map[i]);
            }
            free(map);
            if (pthread_join(input_thread, NULL) < 0) {
                fprintf(stderr, "Error occured while killing thread!\n");
            }
            freeBlocks();
            exit(GAME_FINISHED);
        }
        gameStarted = false;
        return;
    }
    if (ball->y < 0) {
        ball->y = 0;
        ball->yDir = 0;
        prevState.angle = (2 * PI - prevState.angle);
    } else if (ball->y == MAP_HEIGHT) {
        ball->y--;
        ball->yDir--;
        prevState.angle = (2 * PI - prevState.angle);
    } else if (ball->x < 0) {
        ball->x = 0;
        ball->xDir = 0;
        prevState.angle = (2 * PI - prevState.angle) + PI;
    } else if (ball->x == MAP_WIDTH) {
        ball->x = MAP_WIDTH - 1;
        ball->xDir = MAP_WIDTH - 1;
        prevState.angle = (2 * PI - prevState.angle) + PI;
    }
    if (map[ball->y][ball->x] == '#' || map[ball->y][ball->x] == '@' 
		    || map[ball->y][ball->x] == '|' || map[ball->y][ball->x] == '_') {
        if (map[ball->y][ball->x] == '|' || map[ball->y][ball->x] == '_') {
            clearBlock(ball->x, ball->y);
            blocksCount--;
            if (blocksCount == 0) {
                printf("LEVEL %d PASSED. PRESS SPACE TO CONTINUE, Q TO QUIT\n", level);
                gameStarted = false;
                if (level == 1) {
                    level = 2;
                } else if (level == 2) {
                    level = 3;
                }
                freeBlocks();
                initLevel();
                map[prevState.y][prevState.x] = ' ';
                restartLevel();
            }
        }
        if ((ball->x != prevState.x) && (ball->y != prevState.y)) {
            if (map[prevState.y][prevState.x] == map[ball->y][ball->x]) {
                prevState.angle = prevState.angle + PI;
            } else {
                if (map[prevState.y][ball->x] == '#') {
                    prevState.angle = (2 * PI - prevState.angle) + PI;
                } else {
                    prevState.angle = (2 * PI - prevState.angle);
                }
            }
        } else if (ball->y == prevState.y) {
            prevState.angle = (2 * PI - prevState.angle) + PI;
        } else if (ball->y == 0 && (ball->x == MAP_WIDTH || ball->x == 0)) {
            prevState.angle = (2 * PI - prevState.angle);
        } else if (ball->y == MAP_HEIGHT - 2 && (ball->x == MAP_WIDTH || ball->x == 0)) {
            prevState.angle = (2 * PI - prevState.angle);
        }else if (ball->x == MAP_WIDTH - 1 && platform->xTo == MAP_WIDTH - 2
		       && ball->y == MAP_HEIGHT - 1) {
		prevState.angle = (2 * PI - prevState.angle);
	} else {
            prevState.angle = (2 * PI - prevState.angle);
        }
    }
    ball->angle = prevState.angle;
    putBallOnMap(prevState.x, prevState.y);
    if (gameStarted) {
        printMap();
    }
}

void putBallOnMap(int prevX, int prevY) {
    if (prevY == 0 || prevX == 0 || prevX == MAP_WIDTH - 1) {
        map[prevY][prevX] = '#';
    } else if (prevY == platform->y && prevX >= platform->xFrom && prevX <= platform->xTo) {
        map[prevY][prevX] = platform->sign;
    } else {
        map[prevY][prevX] = ' ';
    }
    map[ball->y][ball->x] = ball->sign;
}

int restartLevel() {
    int ret = EXIT_SUCCESS;
    if (lives == 0) {
        return -1;
    }
    char c = map[ball->y][ball->x];
    for (int i = 0; i <= platform->size; ++i) {
        map[platform->y][platform->xFrom + i] = ' ';
    }
    free(platform);
    map[ball->y][ball->x] = c;
    free(ball);
    if (!(platform = initPlatform(map))) {
        fprintf(stderr, "Error occured while creating platform!\n");
        return ALLOC_ERROR;
    }
    if (!(ball = initBall(map, platform))) {
        fprintf(stderr, "Error occured while creating ball!\n");
        return ALLOC_ERROR;
    }
    printMap();
    return ret;
}

void printMap() {
    if (!map) {
        return;
    }
    setCursorPosition(0, 0);
    if (!fb) {
        fb = myMalloc(DISPLAY_WIDTH * DISPLAY_HEIGHT * 2);
    }
    for (int y = 0; y < MAP_HEIGHT; ++y) {
        for (int x = 0; x < MAP_WIDTH; ++x) {
            drawChar(x * CHAR_WIDTH, y * CHAR_HEIGHT, map[y][x]);
        }
    }
    for (int i = 0; i < DISPLAY_WIDTH * DISPLAY_HEIGHT; ++i) {
        parlcd_write_data(parlcd_mem_base, fb[i]);
        fb[i] = BLACK_COLOR;
    }
    printf("LIVES COUNT : %2d\n", lives);
    printf("BLOCKS COUNT : %3d\n", blocksCount);
}

void initLevel() {
    bool blocksAreSet = false;
    if (!blocks && !gameFinished) {
        blocks = myMalloc(sizeof(blocks_t));
    }
    if (!blocks) {
        return;
    }
    blocks->width = 4;
    blocks->height = 2;
    blocks->counter = 0;
    blocks->head = NULL;
    for (int i = 0; i < MAP_HEIGHT; ++i) {
        if (blocksAreSet) {
            break;
        }
        if (level == 1) {
            initBlock(MAP_HEIGHT / 2, 2 * blocks->width, MAX_BLOCKS_ON_ROW);
	    blocksAreSet = true;
        } else if (level == 2) {
            if (i >= 1 && i <= MAX_BLOCKS_ON_ROW) {
                initBlock(i + i, 2 * blocks->width, MAX_BLOCKS_ON_ROW);
            }
        } else if (level == 3) {
            if (i == MAP_HEIGHT / 2 - blocks->width * blocks->height) {
                for (int j = 1; j < MAX_BLOCKS_ON_ROW; ++j) {
                    initBlock(i + j * 2, blocks->width, j);
                }
                blocksAreSet = true;
            }
        }
    }
}

void initBlock(int y, int x, int count) {
    for (int i = 0; i < count; ++i) {
        map[y][x] = '_';
        map[y][x + 1] = '_';
        map[y][x + 2] = '_';
        map[y][x + 3] = '_';
        map[y + 1][x - 1] = '|';
        map[y + 1][x] = '_';
        map[y + 1][x + 1] = '_';
        map[y + 1][x + 2] = '_';
        map[y + 1][x + 3] = '_';
        map[y + 1][x + 4] = '|';
        push(x - 1, x + 4, y, y + 1);
        x += 2 * blocks->width;
        blocksCount++;
    }
}
void clearBlock(int x, int y) {
    if (!blocks) {
        fprintf(stderr, "Blocks llt is empty!\n");
        return;
    }
    block_t *cur = blocks->head;
    bool found = false;
    while (cur != NULL) {
        if (found) {
            break;
        }
        if (cur->xFrom <= x && x <= cur->xTo && (y == cur->yFrom || y == cur->yTo)) {
            for (int xi = cur->xFrom; xi <= cur->xTo; ++xi) {
                map[y][xi] = ' ';
                if (y == cur->yFrom) {
                    map[y + 1][xi] = ' ';
                } else {
                    map[y - 1][xi] = ' ';
                }
            }
            for (int xi = cur->xTo; xi >= cur->xFrom; --xi) {
                map[y][xi] = ' ';
                if (y == cur->yFrom) {
                    map[y + 1][xi] = ' ';
                } else {
                    map[y - 1][xi] = ' ';
                }
            }
            block_t *tmp = cur->next;
            cur = NULL;
            cur = tmp;
            found = true;
            blocks->counter--;
            break;
        } else {
            cur = cur->next;
        }
    }
    printMap();
}

void push(int xFrom, int xTo, int yFrom, int yTo) {
    assert(blocks);
    block_t *newBlock = myMalloc(sizeof(block_t));
    if (!newBlock) {
        return;
    }
    newBlock->xFrom = xFrom;
    newBlock->xTo = xTo;
    newBlock->yFrom = yFrom;
    newBlock->yTo = yTo;
    if (blocks->head) {
        newBlock->next = blocks->head;
    } else {
        newBlock->next = NULL;
        blocks->tail = newBlock;
    }
    blocks->head = newBlock;
    blocks->counter += 1;
}

void freeBlocks() {
    if (!blocks) {
        return;
    }
    if (blocks->counter == 0 && gameFinished) {
        free(blocks);
    } else {
        block_t *cur = blocks->head;
        while (cur->next != NULL) {
            block_t *tmp = cur;
            cur = cur->next;
            free(tmp);
        }
        free(cur);

        if (gameFinished && blocks) {
            free(blocks);
        }
    }
}

void drawChar(int x, int y, char c) {
    int w;
    if (c == platform->sign) {
        w = 14;
    } else if (c == '|' || c == ' ') {
        w = 4;
    } else {
        w = 8;
    };
    int scale = 1;
    int color = c == ' ' ? BLACK_COLOR : WHITE_COLOR;
    const font_bits_t *ptr;
    if ((c >= fdes->firstchar) && (c - fdes->firstchar < fdes->size)) {
        if (fdes->offset) {
            ptr = &fdes->bits[fdes->offset[c - fdes->firstchar]];
        } else {
            int bw = (fdes->maxwidth + 15) / 16;
            ptr = &fdes->bits[(c - fdes->firstchar) * bw * fdes->height];
        }
        int i, j;
        for (i = 0; i < fdes->height; i++) {
            font_bits_t val = *ptr;
            for (j = 0; j < w; j++) {
                if ((val & 0x8000) != 0) {
                    drawPixelBig(x + scale * j, y + scale * i, scale, color);
                }
                val <<= 1;
            }
            ptr++;
        }
    }
}

void drawPixel(int x, int y, int color) {
    if (x >= 0 && x < DISPLAY_WIDTH && y >= 0 && y < DISPLAY_HEIGHT) {
        fb[(y * DISPLAY_WIDTH) + x] = color;
    }
    parlcd_write_cmd(parlcd_mem_base, 0x2c);
}

void drawPixelBig(int x, int y, int scale, int color) {
    for (int i = 0; i < scale; ++i) {
        for (int j = 0; j < scale; ++j) {
            drawPixel(x + i, y + j, color);
        }
    }
}
void *blinkLED() {
    for (int i = 0; i < 3; ++i) {
        if (lives == 3) {
            *(volatile uint32_t *)(led_mem_base + SPILED_REG_LED_RGB1_o) = GREEN_COLOR;
            *(volatile uint32_t *)(led_mem_base + SPILED_REG_LED_RGB2_o) = GREEN_COLOR;
        } else if (lives == 2) {
            *(volatile uint32_t *)(led_mem_base + SPILED_REG_LED_RGB1_o) = YELLOW_COLOR;
            *(volatile uint32_t *)(led_mem_base + SPILED_REG_LED_RGB2_o) = YELLOW_COLOR;
        } else {
            *(volatile uint32_t *)(led_mem_base + SPILED_REG_LED_RGB1_o) = RED_COLOR;
            *(volatile uint32_t *)(led_mem_base + SPILED_REG_LED_RGB2_o) = RED_COLOR;
        }
        sleep(1);
        *(volatile uint32_t *)(led_mem_base + SPILED_REG_LED_RGB1_o) = BLACK_COLOR;
        *(volatile uint32_t *)(led_mem_base + SPILED_REG_LED_RGB2_o) = BLACK_COLOR;
        sleep(1);
    }
    return NULL;
}

