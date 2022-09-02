#ifndef _PRODUCTS_H
#define _PRODUCTS_H
#define NUMBER_OF_STEPS 6
#include "utils.h"

enum place {
    NUZKY,
    VRTACKA,
    OHYBACKA,
    SVARECKA,
    LAKOVNA,
    SROUBOVAK,
    FREZA,
    _PLACE_COUNT
};

static const char *place_str[_PLACE_COUNT] = {
    [NUZKY] = "nuzky",
    [VRTACKA] = "vrtacka",
    [OHYBACKA] = "ohybacka",
    [SVARECKA] = "svarecka",
    [LAKOVNA] = "lakovna",
    [SROUBOVAK] = "sroubovak",
    [FREZA] = "freza",
};

enum product {
    A,
    B,
    C,
    _PRODUCT_COUNT
};

static int productA[NUMBER_OF_STEPS] = {
    [0] = NUZKY,
    [1] = VRTACKA,
    [2] = OHYBACKA,
    [3] = SVARECKA,
    [4] = VRTACKA,
    [5] = LAKOVNA,
};

static int productB[NUMBER_OF_STEPS] = {
    [0] = VRTACKA,
    [1] = NUZKY,
    [2] = FREZA,
    [3] = VRTACKA,
    [4] = LAKOVNA,
    [5] = SROUBOVAK,
};

static int productC[NUMBER_OF_STEPS] = {
    [0] = FREZA,
    [1] = VRTACKA,
    [2] = SROUBOVAK,
    [3] = VRTACKA,
    [4] = FREZA,
    [5] = LAKOVNA,
};

static const char *product_str[_PRODUCT_COUNT] = {
    [A] = "A",
    [B] = "B",
    [C] = "C",
};

typedef struct {
    int *steps;
    int step;
    char productType;
    int indexInQueue;
} product_t;

product_t *createProduct(int type);

#endif
