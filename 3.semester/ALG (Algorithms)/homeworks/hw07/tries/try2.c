#include <time.h>

#include "utils.h"

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    clock_t start = clock();
    int width, eggs_count;
    if ((scanf("%d %d\n", &width, &eggs_count)) != 2) {
        fprintf(stderr, "Error: wrong input!\n");
        return EXIT_FAILURE;
    }
    // printf("Number of eggs: %d\n", eggs_count);
    int *eggs = (int *)malloc(sizeof(int) * ELEMENTS_IN_ARRAY * (eggs_count + 2));
    if (!eggs) {
        fprintf(stderr, alloc_error, __LINE__, __FILE__);
        return -1;
    }
    // read input
    int max_height = 0;
    for (int i = 0; i < eggs_count; ++i) {
        int row, column;
        if ((scanf("%d %d\n", &column, &row)) != 2) {
            fprintf(stderr, "Error: wrong input!\n");
            return -1;
        }
        eggs[(i * ELEMENTS_IN_ARRAY)] = column;
        eggs[(i * ELEMENTS_IN_ARRAY) + 1] = row;
        if (max_height < row) {
            max_height = row;
        }
    }

    // get counters of each time
    int *counters = get_counters(eggs, eggs_count, max_height);
    // compute indexies
    int *indexies = compute_indexies(counters, eggs_count, max_height);
    // write to final array

    int *final_array = sort_eggs(indexies, counters, eggs, eggs_count, max_height);

    //PRINTS
    // print_counters(counters, max_height);
    // print_indexies(indexies, eggs_count, max_height);
    // print_final_array(final_array, eggs_count);

    free(eggs);
    free(indexies);

    int **table = get_table(width);

    int step = 0;
    // int result = 0;
    bool no_changes = false;
    int index_in_table = 0;
    int number_of_positions = width - 2;
    int counter_of_empty_rows = 0;
    for (int i = 0; i < eggs_count; ++i) {
        int next_column = (index_in_table + 1) % 2;
        // if (counter_of_empty_rows >= number_of_positions) {
        //     i--;
        //     step++;
        //     // printf("CLEN!\n");
        //     continue;
        // }
        if (!counters[step]) {  // there is no egg on this row
            printf("There is no egg on step %d!\n", step);
            if (!no_changes) {
                for (int j = 0; j <= number_of_positions; ++j) {
                    if (step + 1 < j) {
                        // printf("stopped at %d\n", j);
                        break;
                    }
                    int score_on_the_left = 0;
                    int score_on_the_right = 0;
                    int score_on_same_position = 0;
                    if (j == 0) {
                        score_on_same_position = table[j][index_in_table];
                        score_on_the_right = table[j + 1][index_in_table];
                        table[j][next_column] = score_on_same_position > score_on_the_right
                                                    ? score_on_same_position
                                                    : score_on_the_right;
                    } else if (j == number_of_positions) {
                        score_on_same_position = table[j][index_in_table];
                        score_on_the_left = table[j - 1][index_in_table];
                        table[j][next_column] = score_on_same_position > score_on_the_left
                                                    ? score_on_same_position
                                                    : score_on_the_left;
                    } else {
                        score_on_same_position = table[j][index_in_table];
                        score_on_the_left = table[j - 1][index_in_table];
                        score_on_the_right = table[j + 1][index_in_table];
                        // if (j == 2) {
                        //     printf("score_left: %d, right: %d, same: %d\n", score_on_the_left, score_on_the_right, score_on_same_position);
                        // }
                        if (score_on_same_position > score_on_the_right) {
                            if (score_on_same_position > score_on_the_left) {
                                table[j][next_column] = score_on_same_position;
                            } else {
                                table[j][next_column] = score_on_the_left;
                            }
                        } else {
                            table[j][next_column] = score_on_the_right > score_on_the_left
                                                        ? score_on_the_right
                                                        : score_on_the_left;
                        }
                        // table[j][next_column] = score_on_same_position > score_on_the_left
                        //                             ? score_on_same_position > score_on_the_right
                        //                                   ? score_on_same_position
                        //                                   : score_on_the_right
                        //                             : score_on_the_left;
                    }
                }
            }
            counter_of_empty_rows++;
            step++;
            i--;
        } else {  // atleats one egg on this row
            counter_of_empty_rows = 0;
            int egg_column = final_array[(i * ELEMENTS_IN_ARRAY)];
            printf("Egg on coords: %d %d\n", final_array[(i * ELEMENTS_IN_ARRAY) + 1], egg_column);
            int egg_on_the_left = 0;
            int egg_on_the_right = 0;
            int egg_on_same_position = 0;
            for (int j = 0; j <= number_of_positions; ++j) {
                if (step + 1 < j) {
                    break;
                } /*else if (!(j >= egg_column - 3 && j <= egg_column - 1)) {
                    if (egg_column == 5) {
                        printf("clen %d!\n", j);
                    }
                    continue;
                }*/
                if (j == 0) {
                    egg_on_same_position = egg_column <= 2
                                               ? table[j][index_in_table] + 1
                                               : table[j][index_in_table];
                    egg_on_the_right = egg_column >= 2 && egg_column <= 3
                                           ? table[j][index_in_table] + 1
                                           : table[j][index_in_table];
                    // printf("same:%d right:%d\n", egg_on_same_position, egg_on_the_right);
                    table[j][next_column] = egg_on_same_position;
                    table[j + 1][next_column] = egg_on_the_right >= table[j + 1][index_in_table]
                                                    ? egg_on_the_right
                                                    : table[j + 1][index_in_table];
                } else if (j == number_of_positions) {
                    egg_on_same_position = egg_column >= width - 1
                                               ? table[j][index_in_table] + 1
                                               : table[j][index_in_table];
                    egg_on_the_left = egg_column >= width - 2 && egg_column <= width - 1
                                          ? table[j][index_in_table] + 1
                                          : table[j][index_in_table];
                    // printf("same:%d left:%d\n", egg_on_same_position, egg_on_the_left);
                    table[j][next_column] = egg_on_same_position >= table[j][index_in_table]
                                                ? egg_on_same_position
                                                : table[j][index_in_table];
                    table[j - 1][next_column] = egg_on_the_left >= table[j - 1][next_column]
                                                    ? egg_on_the_left
                                                    : table[j - 1][next_column];
                } else {
                    egg_on_the_left = egg_column >= j && egg_column <= j + 1
                                          ? table[j][index_in_table] + 1
                                          : table[j][index_in_table];
                    egg_on_same_position = egg_column >= j + 1 && egg_column <= j + 2
                                               ? table[j][index_in_table] + 1
                                               : table[j][index_in_table];
                    egg_on_the_right = egg_column >= j + 2 && egg_column <= j + 3
                                           ? table[j][index_in_table] + 1
                                           : table[j][index_in_table];
                    // if (j == 1) {
                    //     printf("table[j] = %d\n", table[j][index_in_table]);
                    //     printf("left: %d same:%d right:%d\n", egg_on_the_left, egg_on_same_position, egg_on_the_right);
                    // }
                    table[j - 1][next_column] = egg_on_the_left >= table[j - 1][next_column]
                                                    ? egg_on_the_left
                                                    : table[j - 1][next_column];
                    table[j][next_column] = egg_on_same_position >= table[j][next_column]
                                                ? egg_on_same_position
                                                : table[j][next_column];
                    table[j + 1][next_column] = egg_on_the_right >= table[j + 1][next_column]
                                                    ? egg_on_the_right
                                                    : table[j + 1][next_column];
                }
            }
            if (counters[step] <= 1) {
                step++;
            } else {
                counters[step]--;
            }
        }
        print_table(table, width);
        index_in_table = (index_in_table + 1) % 2;
    }
    // printf("result: %d\n", result);
    // free(final_array);
    // free(counters);
    // for (int i = 0; i < width - 1; ++i) {
    //     free(table[i]);
    // }
    // free(table);
    clock_t end = clock();
    printf("TIME: %f\n", (double)(end - start) / CLOCKS_PER_SEC);
    return ret;
}
