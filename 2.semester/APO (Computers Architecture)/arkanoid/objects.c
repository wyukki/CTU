#include "utils.h"
#include "objects.h"

char **initMap()
{
    char **map = myMalloc(MAP_HEIGHT * MAP_WIDTH);
    if (!map)
    {
        return NULL;
    }
    for (int i = 0; i < MAP_HEIGHT; ++i)
    {
        map[i] = myMalloc(MAP_WIDTH);
        if (!map[i])
        {
            free(map);
            return NULL;
        }
        for (int j = 0; j < MAP_WIDTH; ++j)
        {
            if (i == 0 || i == MAP_HEIGHT - 1 || j == 0 || j == MAP_WIDTH - 1)
            {
                map[i][j] = '#';
            }
            else
            {
                map[i][j] = ' ';
            }
        }
    }
    return map;
}
platform_t *initPlatform(char **map)
{
    platform_t *platform = myMalloc(sizeof(platform_t));
    if (!platform)
    {
        return NULL;
    }
    platform->size = round(MAP_WIDTH / 3.0);
    platform->xFrom = MAP_WIDTH / 2 - platform->size / 2;
    platform->xTo = MAP_WIDTH / 2 + platform->size / 2;
    platform->y = MAP_HEIGHT - 2;
    platform->sign = '@';
    for (int i = platform->xFrom; i <= platform->xTo; ++i)
    {
        map[platform->y][i] = platform->sign;
    }
    return platform;
}

ball_t *initBall(char **map, platform_t *platform)
{
    ball_t *ball = myMalloc(sizeof(ball_t));
    if (!ball)
    {
        return NULL;
    }
    ball->x = platform->xFrom + platform->size / 2;
    ball->y = platform->y - 1;
    ball->xDir = ball->x;
    ball->yDir = ball->y;
    ball->velocity = 1.3;
    ball->angle = ((double)rand() / RAND_MAX) - 1 /** (2 / PI)*/;
    ball->sign = 'o';
    map[ball->y][ball->x] = ball->sign;
    return ball;
}
