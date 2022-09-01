#include "utils.h"

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    int width, eggs_count;
    if ((scanf("%d %d\n", &width, &eggs_count)) != 2) {
        fprintf(stderr, "Error: wrong input!\n");
        return EXIT_FAILURE;
    }
    // printf("Number of eggs: %d\n", eggs_count);
    int *eggs = (int *)malloc(sizeof(int) * 2 * (eggs_count + 1));
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
    print_counters(counters, max_height);
    // print_indexies(indexies, eggs_count, max_height);
    // print_final_array(final_array, eggs_count);

    free(eggs);
    free(indexies);

    int **table = get_table(width);

    int step = 0;
    int result = 0;
    bool no_changes = true;
    for (int i = 0; i < eggs_count; ++i) {
        int index_in_table = step % 2;
        if (!counters[step]) {  // no egg on this row
            // printf("No eggs on row: %d\n", step);
            if (!no_changes && i != 0) {
                for (int j = 0; j < width - 2; ++j) {
                    if (table[j][(index_in_table - 1) % 2] > table[j + 1][(index_in_table - 1) % 2]) {
                        table[j + 1][index_in_table] = table[j][(index_in_table - 1) % 2];
                        no_changes = false;
                        // printf("first if new value: %d\n", table[j][index_in_table]);
                        // printf("j = %d\n", j);
                    } else if (table[j][(index_in_table - 1) % 2] < table[j + 1][(index_in_table - 1) % 2]){
                        table[j][index_in_table] = table[j + 1][(index_in_table - 1) % 2];
                        no_changes = false;
                        // printf("second if new value: %d\n", table[j][index_in_table]);
                        // printf("j = %d\n", j);
                    } else if (!no_changes) {
                        no_changes = true;                        
                    }
                }
                printf("\nTable without eggs on row:\n");
                print_table(table, width);
            } else {
                printf("\nNo changes since last row!\n");
                step++;
                continue;
            }
        } else {  // atleast on egg on this row
            no_changes = false;
            printf("Egg on position %dx%d\n", eggs[(i * ELEMENTS_IN_ARRAY) + 1], eggs[(i * ELEMENTS_IN_ARRAY)]);
            printf("\nstep = %d\n", step);
            int egg_column = final_array[(i * ELEMENTS_IN_ARRAY)];
            for (int j = 0; j < width - 2; ++j) {
                if (step + 1 < j) {  // cannot take this position yet
                    break;
                }
                int egg_on_the_left = 0;
                int egg_on_the_right = 0;
                int egg_on_same_position = 0;

                if (j == 0) {
                    egg_on_the_left = 0;

                    egg_on_same_position = egg_column <= 2
                                               ? table[j][index_in_table] + 1
                                               : table[j][index_in_table];

                    egg_on_the_right = egg_column >= 2 && egg_column <= 3
                                           ? table[j + 1][index_in_table] + 1
                                           : table[j + 1][index_in_table];

                    table[j][(index_in_table + 1) % 2] = egg_on_same_position;
                    table[j + 1][(index_in_table + 1) % 2] = egg_on_the_right;

                } else if (j == width - 2) {
                    egg_on_the_right = 0;

                    egg_on_same_position = egg_column >= width - 1
                                               ? table[j][index_in_table] + 1
                                               : egg_on_same_position;

                    egg_on_the_left = egg_column >= width - 2 && egg_column < width
                                          ? table[j - 1][index_in_table] + 1
                                          : table[j - 1][index_in_table];

                    table[j - 1][(index_in_table + 1) % 2] = egg_on_the_left >= table[j - 1][index_in_table]
                                                       ? egg_on_the_left
                                                       : table[j - 1][index_in_table];
                    table[j][(index_in_table + 1) % 2] = egg_on_same_position >= table[j][index_in_table]
                                                   ? egg_on_same_position
                                                   : table[j][index_in_table];
                } else {
                    egg_on_the_left = egg_column >= j && egg_column <= j + 1
                                          ? egg_on_the_left + 1
                                          : egg_on_the_left;

                    egg_on_same_position = egg_column >= j + 1 && egg_column <= j + 2
                                               ? egg_on_same_position + 1
                                               : egg_on_same_position;

                    egg_on_the_right = egg_column >= j + 2 && egg_column <= j + 3
                                           ? egg_on_the_right + 1
                                           : egg_on_the_right;
                    table[j - 1][(index_in_table + 1) % 2] = egg_on_the_left >= table[j - 1][index_in_table]
                                                       ? egg_on_the_left
                                                       : table[j - 1][index_in_table];
                    table[j][(index_in_table + 1) % 2] = egg_on_same_position >= table[j][index_in_table]
                                                   ? egg_on_same_position
                                                   : table[j][index_in_table];
                    // printf("%d\n", j + 1);
                    table[j + 1][(index_in_table + 1) % 2] = egg_on_the_right;
                }
            }
            printf("\nTable:\n");
            print_table(table, width);
        }
        if (i == eggs_count - 1) {  // find max
            for (int j = 0; j < width - 1; ++j) {
                if (table[j][index_in_table] > result) {
                    result = table[j][index_in_table];
                }
            }
        }
        step++;
    }
    printf("result: %d\n", result);
    free(final_array);
    free(counters);
    for (int i = 0; i < width - 1; ++i) {
        free(table[i]);
    }
    free(table);
    return ret;
}
