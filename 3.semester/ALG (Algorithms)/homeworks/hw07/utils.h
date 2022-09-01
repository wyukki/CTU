#ifndef _UTILS_H
#define _UTILS_H
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define ELEMENTS_IN_ARRAY 2

int *get_counters(int *eggs, int eggs_count, int max_height);
int *compute_indexies(int *counters, int eggs_count, int max_height);
int *sort_eggs(int *indexies, int *counters, int *eggs, int eggs_count, int max_height);

void print_eggs(int *eggs, int eggs_count);
void print_final_array(int *final_array, int eggs_count);
void print_indexies(int *indexies, int eggs_count, int max_height);
void print_counters(int *counters, int max_height);
void print_table(int **table, int width);
int **get_table(int width);
int solve_problem(int **table, int *fin_array, int *counters, int number_of_positions, int eggs_count);
void compare_table(int **table, int index_in_table, int next_column, int step, int number_of_positions);

static const char *const alloc_error = "Allocation error on line %d in file %s!\n";

#endif
