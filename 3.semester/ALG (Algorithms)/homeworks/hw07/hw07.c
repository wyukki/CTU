#include "utils.h"
#include <time.h>

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

    free(eggs);
    free(indexies);

    int **table = get_table(width);
    
    int number_of_positions = width - 1;
    int result = solve_problem(table, final_array, counters, number_of_positions, eggs_count);
    printf("%d\n", result);
    free(final_array);
    free(counters);
    for (int i = 0; i < width - 1; ++i) {
        free(table[i]);
    }
    free(table);
    clock_t end = clock();
    printf("TIME: %f\n", (double) (end - start) / CLOCKS_PER_SEC);
    return ret;
}
