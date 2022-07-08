#include <stdio.h>
#include <stdlib.h>
#define WIGHT_SIZE_ERROR 101
#define HEIGHT_SIZE_ERROR 101
#define WIDTH_NOT_ODD 102
#define FENCE_SIZE_ERROR 103

enum {
    MANDATORY,
    OPTIONAL,
    ERROR_WRONG_INPUT = 100
};

/**
 * Tests if width and heigth of a house is valid.
 */
int testHouseDim(int, int);
/**
 * Tests if fence size is valid.
 */
int testFenceDim(int, int, int);
/**
 * Prints a house in ASCII.
 */
int printHouse(int, int);
/**
 * Prints a house with a fence in ASCII.
 */
int printHouseFence(int, int, int);
/**
 * Takes an input from user.
 */
int readHouseInput(int *width, int *height, int *fence_width);

/**
 * A programm, which takes an input from user and displaying a house in ASCII alphabet.
 * Optional part: displaying a fence near to house.
 * Messages are in czech due to evaluator.
 */
int main(int argc, char *argv[]) {
    int width, height, fence_wight;
    int ret = 0;
    switch (ret = readHouseInput(&width, &height, &fence_wight)) {
        case MANDATORY:
            ret = printHouse(width, height);
            break;
        case OPTIONAL:
            ret = printHouseFence(width, height, fence_wight);
            break;
    }
    return ret;
}

int readHouseInput(int *widthp, int *heightp, int *fence_widthp) {
    int ret = ERROR_WRONG_INPUT;
    if (scanf("%d %d", widthp, heightp) == 2) {
        ret = MANDATORY;
    } else {
        fprintf(stderr, "Error: Chybny vstup!\n");  // error: wrong input
        return ret;
    }
    if (ret == MANDATORY && *widthp == *heightp) {  // if width of a house and it's height are equal, need to create a fence
        if (scanf("%d", fence_widthp) == 1) {
            ret = OPTIONAL;
        } else {
            ret = ERROR_WRONG_INPUT;
            fprintf(stderr, "Error: Chybny vstup!\n");  // error: wrong input
            return ret;
        }
    }
    return ret;
}

int printHouse(int width, int height) {
    int ret = testHouseDim(width, height);
    if (ret) return ret;
    int x, y;
    int z, k;
    int l = 0;

    for (k = 1; k <= (width / 2); k++) {
        for (z = 1; z <= width; z++) {
            if (k != 1 && z == (width / 2) - l) {
                l += 1;
                printf("X");
            } else {
                printf(" ");
            }
            if (z == (width / 2) + l) {
                printf("X\n");
                break;
            }
        }
    }

    for (y = 1; y <= height; y++) {
        for (x = 1; x <= width; x++) {
            if (y > 1 && y < height) {
                if (x == 1 || x == width) {
                    printf("X");
                } else {
                    printf(" ");
                }
            } else {
                printf("X");
            }
        }
        printf("\n");
    }
    return ret;
}

int printHouseFence(int width, int height, int fence_width) {
    int ret = testHouseDim(width, height);
    if (ret == 0) {
        ret = testFenceDim(width, height, fence_width);
    }
    if (ret == 0) {
        int x, y;
        int z, k;
        int l = 0;
        int o, g;
        if (ret == 0) {
            o = (height - fence_width) + 1;
            for (k = 1; k <= (width / 2); k++) {
                for (z = 1; z <= width; z++) {
                    if (k != 1 && z == (width / 2) - l) {
                        l += 1;
                        printf("X");
                    } else {
                        printf(" ");
                    }
                    if (z == (width / 2) + l) {
                        printf("X\n");
                        break;
                    }
                }
            }
            for (y = 1; y <= height; y++) {
                for (x = 1; x <= width; x++) {
                    if (y != 1 && y != height && x != 1 && x != width) {
                        if (x <= width - 1) {
                            if (((x - y) % 2) == 0) {
                                printf("o");
                            } else {
                                printf("*");
                            }
                        }
                    } else {
                        printf("X");
                        while (o == y && x == width) {
                            for (g = 1; g <= fence_width; g++) {
                                if (fence_width % 2 == 0) {
                                    if (o == height || y == ((height - fence_width) + 1)) {
                                        if (g % 2 == 0) {
                                            printf("|");
                                        } else {
                                            printf("-");
                                        }
                                    } else {
                                        if (g % 2 == 0) {
                                            printf("|");
                                        } else {
                                            printf(" ");
                                        }
                                    }
                                } else {
                                    if (o == height || y == ((height - fence_width) + 1)) {
                                        if (g % 2 == 0) {
                                            printf("-");
                                        } else {
                                            printf("|");
                                        }
                                    } else {
                                        if (g % 2 == 0) {
                                            printf(" ");
                                        } else {
                                            printf("|");
                                        }
                                    }
                                }
                            }
                            o += 1;
                        }
                    }
                }
                printf("\n");
            }
        }
    }
    return ret;
}
int testHouseDim(int width, int height) {
    int ret = 0;
    if (width < 3 || width > 69) {
        fprintf(stderr, "Error: Vstup mimo interval!\n");  // error: input is not in interval
        return WIGHT_SIZE_ERROR;
    }
    if (height < 3 || height > 69) {
        fprintf(stderr, "Error: Vstup mimo interval!\n");  // error: input is not in interval
        return HEIGHT_SIZE_ERROR;
    }
    if (width % 2 == 0) {
        fprintf(stderr, "Error: Sirka neni liche cislo!\n");  // error: width is not odd
        return WIDTH_NOT_ODD;
    }

    return ret;
}
int testFenceDim(int width, int height, int fence_width) {
    int ret = 0;
    if (fence_width <= 0 || fence_width >= height) {
        ret = FENCE_SIZE_ERROR;
        fprintf(stderr, "Error: Neplatna velikost plotu!\n");  // error: invalid fence size
    }
    return ret;
}
