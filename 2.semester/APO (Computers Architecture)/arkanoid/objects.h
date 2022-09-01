#ifndef OBJECTS_H
#define OBJECTS_H
#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>

typedef struct block
{
    int xFrom;          // starting x coordinate of block
    int xTo;            // ending x coordinate of block
    int yFrom;          // starting y coordinate of block
    int yTo;            // ending y coordinate of block
    struct block *next; // pointer on next block
} block_t;


typedef struct blocks
{
    int width;  // width of one block
    int height; // height of one block
    int counter;      // current amount of blocks in linked list
    block_t *head;    // pointer on first block
    block_t *tail;    //pointer on last block
} blocks_t;


typedef struct platform
{
    int xFrom; // starting x coordinate of platform
    int y;     // y coordinate of platform
    int xTo;   // ending x coordinate of platform
    int size;  // size of platform
    char sign; // char used for platform
} platform_t;


typedef struct
{
    int x, y;               // x, y coordinate of ball
    char sign;              // cahr used for ball
    double xDir, yDir;    // x, y coordinate, which depends on angle and velocity
    double velocity, angle; // ball's moving angle and velocity

} ball_t;

/**
 * Creates and return starting map
 */
char **initMap();

/**
 * Creates platform, that can be moved by player
 * @map - current map
 * returns platform
 */
platform_t *initPlatform(char **map);

/**
 * Creates ball
 * @map - current map
 */
ball_t *initBall(char **map, platform_t *platform);

#endif