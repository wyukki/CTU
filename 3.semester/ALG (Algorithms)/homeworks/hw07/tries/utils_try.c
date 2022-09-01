#include "utils.h"

#define COLUMNS_IN_TABLE 2

void print_eggs(int *eggs, int eggs_count) {
    for (int i = 0; i < eggs_count; ++i) {
        printf("Egg no. %d: %d %d\n", i, eggs[(i * ELEMENTS_IN_ARRAY) + 1], eggs[(i * ELEMENTS_IN_ARRAY)]);
    }
}

int *get_counters(int *eggs, int eggs_count, int max_height) {
    int *counters = calloc(max_height, sizeof(int));
    if (!counters) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return NULL;
    }
    for (int i = 0; i < eggs_count; ++i) {
        counters[eggs[(i * ELEMENTS_IN_ARRAY) + 1] - 1]++;
    }
    return counters;
}

int *compute_indexies(int *counters, int eggs_count, int max_height) {
    int *indexies = malloc(sizeof(int) * max_height);  // represents the final index
    if (!indexies) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return NULL;
    }
    for (int i = 0; i < max_height; ++i) {
        if (!counters[i]) {
            if (i == 0) {
                indexies[i] = 0;
            } else {
                indexies[i] = indexies[i - 1];
            }
        } else if (i == 0) {
            indexies[i] = counters[i];
        } else {
            indexies[i] = indexies[i - 1] + counters[i];
        }
    }
    return indexies;
}

int *sort_eggs(int *indexies, int *counters, int *eggs, int eggs_count, int max_height) {
    int *counters_copy = malloc(sizeof(int *) * max_height);
    memcpy(counters_copy, counters, sizeof(int) * max_height);
    int *final_array = malloc(sizeof(int) * 2 * eggs_count);
    if (!final_array) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return NULL;
    }
    for (int i = 0; i < eggs_count; ++i) {
        int column_of_egg = eggs[(i * ELEMENTS_IN_ARRAY)];
        int row_of_egg = eggs[(i * ELEMENTS_IN_ARRAY) + 1];
        if (!counters_copy[row_of_egg - 1]) {
            printf("There is not an egg with time %d!\n", i);
            continue;
        }
        int index_of_row = indexies[row_of_egg - 1];
        final_array[(index_of_row - counters_copy[row_of_egg - 1]) * ELEMENTS_IN_ARRAY] = column_of_egg;
        final_array[(index_of_row - counters_copy[row_of_egg - 1]) * ELEMENTS_IN_ARRAY + 1] = row_of_egg;
        counters_copy[row_of_egg - 1]--;
    }
    free(counters_copy);
    return final_array;
}

void print_final_array(int *final_array, int eggs_count) {
    printf("FIN arr:\n");
    for (int i = 0; i < eggs_count; ++i) {
        printf("final[%d]: %d %d\n", i, final_array[(i * ELEMENTS_IN_ARRAY)], final_array[(i * ELEMENTS_IN_ARRAY) + 1]);
    }
    printf("\n");
}
void print_indexies(int *indexies, int eggs_count, int max_height) {
    printf("\nINDEXIES:\n");
    for (int i = 0; i < max_height; ++i) {
        printf("index[%d] = %d\n", i, indexies[i]);
    }
    printf("\n");
}

void print_counters(int *counters, int max_height) {
    printf("\nCOUNTERS:\n");
    for (int i = 0; i < max_height; ++i) {
        printf("Eggs on time %d: %d\n", i, counters[i]);
    }
    printf("\n");
}

int **get_table(int width) {
    int **table = malloc(sizeof(int *) * (width - 1));
    if (!table) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return NULL;
    }
    for (int i = 0; i < width - 1; ++i) {
        table[i] = malloc(sizeof(int) * COLUMNS_IN_TABLE);
        if (!table[i]) {
            fprintf(stderr, alloc_error, __LINE__, __FILE__);
            return NULL;
        }
        memset(table[i], 0, sizeof(int) * COLUMNS_IN_TABLE);
    }
    return table;
}

void print_table(int **table, int width) {
    for (int i = 0; i < width - 1; ++i) {
        printf("Position %d: ", i);
        for (int j = 0; j < COLUMNS_IN_TABLE; ++j) {
            printf("%d ", table[i][j]);
        }
        printf("\n");
    }
}

int solve_problem(int **table, int *fin_array, int *counters,
                  int number_of_positions, int eggs_count) {
    int result = 0;
    int step = 0;
    int number_of_empty_rows = 0;
    int index_in_table = 0;
    int prev_row = 0;
    for (int i = 0; i < eggs_count; ++i) {
        int next_column = (index_in_table + 1) % 2;
        if (!counters[step]) {
            printf("No egg on row %d\n", step);
            if (number_of_empty_rows >= number_of_positions) {
                printf("Too many empty rows!\n");
            } else {
                if (step == 5) {
                    printf("index_in_table: %d, next: %d\n", index_in_table, next_column);
                }
                for (int j = 0; j < number_of_positions; ++j) {
                    if (step + 1 < j) {
                        break;
                    }
                    int curr_score = table[j][index_in_table];
                    if (j == 0) {
                        int score_on_the_right = table[j + 1][index_in_table];
                        table[j][next_column] = curr_score;
                        if (score_on_the_right > curr_score) {
                            table[j][next_column] = score_on_the_right;
                        } else {
                            table[j + 1][next_column] = curr_score;
                        }
                    } else if (j != number_of_positions - 1) {
                        int score_on_the_left = table[j - 1][index_in_table];
                        int score_on_the_right = table[j + 1][index_in_table];
                        if (curr_score >= score_on_the_left) {
                            table[j - 1][next_column] = curr_score;
                        } else {
                            table[j][next_column] = score_on_the_left;
                        }
                        if (curr_score >= score_on_the_right) {
                            table[j + 1][next_column] = curr_score;
                        } else {
                            table[j][next_column] = score_on_the_right;
                        }
                    } else {
                        int score_on_the_left = table[j - 1][index_in_table];
                        if (curr_score >= score_on_the_left) {
                            table[j - 1][next_column] = curr_score;
                        } else {
                            table[j][next_column] = score_on_the_left;
                        }
                    }
                }
            }
            i--;
            number_of_empty_rows++;
        } else {
            number_of_empty_rows = 0;
            int egg_column = fin_array[(i * ELEMENTS_IN_ARRAY)];
            int egg_row = fin_array[(i * ELEMENTS_IN_ARRAY) + 1];
            int egg_on_the_left = 0;
            int egg_on_the_right = 0;
            int egg_on_same_position = 0;
            printf("Egg coords: %d %d\n", egg_row, egg_column);
            for (int j = 0; j < number_of_positions; ++j) {
                if (step + 1 < j) {
                    break;
                } else if (prev_row == egg_row && j + 1 < egg_column - 1) {
                    // printf("j stopped at: %d\n", j);
                    continue;
                }
                int curr_position = table[j][index_in_table];
                if (j == 0) {
                    egg_on_same_position = egg_column <= 2 ? curr_position + 1 : curr_position;
                    egg_on_the_right = egg_column >= 2 && egg_column <= 3
                                           ? curr_position + 1
                                           : curr_position;
                    // if (egg_column == 1 && egg_row == 4) {
                    //     printf("curr: %d, right: %d, same: %d\n", curr_position, egg_on_the_right, egg_on_same_position);
                    // }
                    table[j][next_column] = egg_on_same_position;
                    table[j + 1][next_column] = egg_on_the_right > table[j + 1][next_column]
                                                    ? egg_on_the_right
                                                    : table[j + 1][next_column];
                } else if (j != number_of_positions - 1) {
                    egg_on_same_position = egg_column >= j + 1 && egg_column <= j + 2
                                               ? curr_position + 1
                                               : curr_position;
                    egg_on_the_left = egg_column >= j && egg_column <= j + 1
                                          ? curr_position + 1
                                          : curr_position;
                    egg_on_the_right = egg_column >= j + 2 && egg_column <= j + 3
                                           ? curr_position + 1
                                           : curr_position;
                    // if (egg_column == 1 && egg_row == 4) {
                    //     printf("curr: %d, right: %d, same: %d, left: %d\n", curr_position, egg_on_the_right, egg_on_same_position, egg_on_the_left);
                    // }
                    table[j - 1][next_column] = egg_on_the_left >= table[j - 1][next_column]
                                                    ? egg_on_the_left
                                                    : table[j - 1][next_column];
                    table[j][next_column] = egg_on_same_position >= table[j][next_column]
                                                ? egg_on_same_position
                                                : table[j][next_column];
                    table[j + 1][next_column] = egg_on_the_right >= table[j + 1][next_column]
                                                    ? egg_on_the_right
                                                    : table[j][next_column];
                } else {
                    egg_on_same_position = egg_column >= number_of_positions
                                               ? curr_position + 1
                                               : curr_position;
                    egg_on_the_left = egg_column >= number_of_positions - 1 && egg_column <= number_of_positions
                                          ? curr_position + 1
                                          : curr_position;
                    table[j - 1][next_column] = egg_on_the_left >= table[j - 1][next_column]
                                                    ? egg_on_the_left
                                                    : table[j - 1][next_column];
                    table[j][next_column] = egg_on_same_position >= table[j][next_column]
                                                ? egg_on_same_position
                                                : table[j][next_column];
                }
            }
        }
        prev_row = fin_array[(i * ELEMENTS_IN_ARRAY) + 1];
        print_table(table, number_of_positions + 1);
        printf("-----------\n");
        if (counters[step] <= 1) {
            step++;
            index_in_table = (index_in_table + 1) % 2;
        } else {
            counters[step]--;
        }
    }
    return result;
}
