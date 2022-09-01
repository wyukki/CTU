#ifndef UTILS_H
#define UTILS_H
#include <assert.h>
#include <ctype.h>
#include <math.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <termios.h>
#include <time.h>
#include <string.h>
#include <unistd.h>
#include "font_types.h"
#include "mzapo_parlcd.h"
#include "mzapo_phys.h"
#include "mzapo_regs.h"
#include "objects.h"
#define DISPLAY_WIDTH 480
#define DISPLAY_HEIGHT 320
#define MAP_HEIGHT 20
#define MAP_WIDTH 60
#define BLACK_COLOR 0x000000
#define WHITE_COLOR 0xffffff
#define RED_COLOR 0xff0000
#define YELLOW_COLOR 0xffff00
#define GREEN_COLOR 0x00ff00

/**
 * Allocates memory
 * size - size of block to be allocated 
 * 
**/
void *myMalloc(int size);

/**
 * Sets terminal to non-blocking "raw" mode 
 */
void setTerminalNonBlock(bool gameFinished);

/**
 * Set cursor to given position
 * @xPos - x coordinate of new position
 * @yPos - y coordinate of new position
 */
void setCursorPosition(int x, int y);

/**
 * Sets every element of frame buffer to 0 
*/
void setDisplayBlack(unsigned char *parlcd_mem_base, unsigned short *fb);

/**
 * Calls setTerminalNonBlock and setDisplayBlack functons
 * @gameFinished - true if game is finished, else false
 * @parlcd_mem_base - physical address of LCD display
 * @fb - frame buffer
*/
void finishGame(bool gameFinished, unsigned char *parlcd_mem_base, unsigned short *fb);

/**
 * Frees all objects used for game
 * @map - current map
 * @platform - platform, that can be moved by player
 * @ball - ball
 * @fb - frame buffer
 */
void freeAllObjects(char **map, platform_t *platform, ball_t *ball, unsigned short *fb);

#endif
