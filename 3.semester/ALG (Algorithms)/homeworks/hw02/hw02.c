#include "utils.h"

int **matrix;
int *patterns;
int patternTypes;
int rows, columns;
int max = 0;

/**
 * Heuristic! Sorts patterns from bigger to smaller
 */
void sortPatterns();

/**
 * Checks if there is a possible pattern by iterating through matrix
 */
bool isPossiblePattern(int **fakeMatrix, int i, int j);
/**
 * Main function. Recursivly builds a houses
 */
void buildAHouse(int **fakeMatrix, int sum, int i, int j);
/**
 * Marks in fakeMatrix (where new houses are added) the occupied places.
 * Calls a markNeighbor function
 */
int fillMatrix(int **fakeMatrix, int i, int j, int patternType);
/**
 * Marks all near indexies in fake matrix as -1 (the place, where a house can't be build)
 */
void markNeighbor(int **fakeMatrix, int y, int x);
/**
 * Returns a sum of unoccupied places
 */
int computeRest(int **fakeMatrix, int i, int j);
/**
 * Iterates through matrix and returns true, if atleast one house can be build,
 * false otherwise
 */
bool canBuildAHouse(int **fakeMatrix, int i, int j, int patternType);
/**
 * Returns fake matrix to a previous state
 */
void undo(int **fakeMatrix, int **copyOfMatrix, int i, int j, int patternType);
/**
 * Creates a copy of fakeMatrix, which is used to undo the changes
 */
int **createCopyOfMatrix(int **fakeMatrix);

/**
 * We have a field, where each element has value - a cost to build house on that place.
 * There can be multiple kinds of houses, that can be build, those kinds I call "patterns".
 * The goal is to place houses that way, that the total cost will be the biggest.
 *
 * There are some rules:
 * 1. we don't need to place all houses on field,
 * 2. there should be a space between every 2 houses (i.e. 2 houeses cannot be place near to each other,
 * between them should be at least one free square),
 * 3. values on the field are always in decreasing order from left-upper element to right-bottom element
 * (i.e. field[0][0] always has the biggest value, and field[height][width] the smallest).
 *
 * There is an image, that illustrates the problem in directory "data"
 * 
 * Program recieves input from user:
 * 1. number of rows and columns in matrix
 * 2. field, that is represented by matrix, where each element has a number - the cost of this place
 * 3. number of house "patterns", that will be used
 * 4. patterns themselves: first number is number of concrete pattern, second - it's width, third - it's height
 * 
 * Assignment: https://cw.felk.cvut.cz/brute/data/ae/release/2021z_b4b33alg/alg_cz_2021z/evaluation/input.php?task=grounds
 * (only in czech)
 */

int main(int argc, char const *argv[]) {
    int ret = EXIT_SUCCESS;
    if ((scanf("%d %d\n", &rows, &columns)) != 2) {
        fprintf(stderr, "Error: wrong input!\n");
        return -1;
    }
    int sum = 0;
    matrix = getMatrix(&sum, rows, columns);
    if ((scanf("%d\n", &patternTypes)) != 1) {
        fprintf(stderr, "Error: wrong input!\n");
        return -1;
    }
    patterns = malloc(sizeof(int) * ELEMENTS_IN_ONE_PATTERN * patternTypes);
    if (!patterns) {
        fprintf(stderr, alloc_error, __LINE__);
        for (int i = 0; i < rows; ++i) {
            free(matrix[i]);
        }
        free(matrix);
        return -1;
    }
    for (int i = 0; i < patternTypes; ++i) {
        int index = i * ELEMENTS_IN_ONE_PATTERN;
        if ((scanf("%d %d %d\n", &patterns[index], &patterns[index + 1], &patterns[index + 2])) != 3) {
            fprintf(stderr, "Error: wrong input!\n");
            return -1;
        }
    }
    if (patternTypes > 1) {
        sortPatterns();
    }
    int **fakeMatrix = malloc(sizeof(int *) * rows);
    if (!fakeMatrix) {
        fprintf(stderr, alloc_error, __LINE__);
        return -1;
    }
    for (int i = 0; i < rows; ++i) {
        fakeMatrix[i] = malloc(sizeof(int) * columns);
        if (!fakeMatrix[i]) {
            fprintf(stderr, alloc_error, __LINE__);
            return -1;
        }
        memcpy(fakeMatrix[i], matrix[i], sizeof(int) * columns);
    }
    buildAHouse(fakeMatrix, 0, 0, 0);
    printf("%d\n", max);
    for (int i = 0; i < rows; ++i) {
        free(matrix[i]);
        free(fakeMatrix[i]);
    }
    free(matrix);
    free(fakeMatrix);
    free(patterns);
    return ret;
}

void sortPatterns() {
    for (int i = 0; i < patternTypes - 1; ++i) {
        for (int j = 0; j < patternTypes - i - 1; ++j) {
            int indexI = j * ELEMENTS_IN_ONE_PATTERN;
            int indexJ = (j + 1) * ELEMENTS_IN_ONE_PATTERN;
            int areaI = patterns[indexI] * patterns[indexI + 1];
            int areaJ = patterns[indexJ] * patterns[indexJ + 1];
            if (areaI < areaJ) {
                int tmpHeight = patterns[indexI];
                int tmpWidth = patterns[indexI + 1];
                int tmpNumberOfPatterns = patterns[indexI + 2];
                patterns[indexI] = patterns[indexJ];
                patterns[indexI + 1] = patterns[indexJ + 1];
                patterns[indexI + 2] = patterns[indexJ + 2];
                patterns[indexJ] = tmpHeight;
                patterns[indexJ + 1] = tmpWidth;
                patterns[indexJ + 2] = tmpNumberOfPatterns;
            }
        }
    }
}

void buildAHouse(int **fakeMatrix, int sum, int i, int j) {
    if (!isPossiblePattern(fakeMatrix, i, j)) {
        if (sum > max) {
            max = sum;
        }
        return;
    }
    int prevSum = sum;
    sum += computeRest(fakeMatrix, i, j + 2);
    if (sum <= max) {
        return;
    }
    int **copyOfFakeMatrix = createCopyOfMatrix(fakeMatrix);
    for (int y = i; y < rows; ++y) {
        for (int x = 0; x < columns; ++x) {
            for (int k = 0; k < patternTypes; ++k) {
                if (!canBuildAHouse(fakeMatrix, y, x, k)) {
                    continue;
                }
                // build a house
                int area = fillMatrix(fakeMatrix, y, x, k);
                if (!area) {
                    continue;
                }
                // recursion
                buildAHouse(fakeMatrix, area + prevSum, y, x);
                // undo changes
                undo(fakeMatrix, copyOfFakeMatrix, y, x, k);
                patterns[(k * ELEMENTS_IN_ONE_PATTERN) + 2]++;
                int tmp = computeRest(fakeMatrix, i, j + 2);
                if ((prevSum + tmp - area) <= max) {
                    return;
                }
            }
            if (!prevSum) {
                break;
            }
        }
        if (!prevSum) {
            break;
        }
    }
    for (int k = 0; k < rows; ++k) {
        free(copyOfFakeMatrix[k]);
    }
    free(copyOfFakeMatrix);
}

int fillMatrix(int **fakeMatrix, int i, int j, int patternType) {
    int area = 0;
    int height = patterns[(patternType * ELEMENTS_IN_ONE_PATTERN)];
    int width = patterns[(patternType * ELEMENTS_IN_ONE_PATTERN) + 1];
    if (!patterns[(patternType * ELEMENTS_IN_ONE_PATTERN) + 2]) {
        return 0;
    }
    patterns[(patternType * ELEMENTS_IN_ONE_PATTERN) + 2]--;
    for (int y = i; y < i + height; ++y) {
        for (int x = j; x < j + width; ++x) {
            area += fakeMatrix[y][x];
            fakeMatrix[y][x] = 0;
        }
    }
    // mark near indexies as -1
    for (int y = i - 1; y < i + height + 1; y++) {
        for (int x = j - 1; x < j + width + 1; x++) {
            markNeighbor(fakeMatrix, y, x);
        }
    }
    return area;
}

bool isPossiblePattern(int **fakeMatrix, int i, int j) {
    int x = j;
    for (int y = i; y < rows; ++y) {
        for (; x < columns; ++x) {
            for (int k = 0; k < patternTypes; ++k) {
                if (canBuildAHouse(fakeMatrix, y, x, k)) {
                    return true;
                }
            }
        }
        x = 0;
    }
    return false;
}

int computeRest(int **fakeMatrix, int i, int j) {
    int rest = 0;
    int x = j;
    for (int y = i; y < rows; ++y) {
        for (; x < columns; ++x) {
            if (fakeMatrix[y][x] <= 0) {
                continue;
            }
            rest += fakeMatrix[y][x];
        }
        x = 0;
    }
    return rest;
}
void markNeighbor(int **fakeMatrix, int y, int x) {
    if (y < 0 || x < 0 || y >= rows || x >= columns || !fakeMatrix[y][x]) {
        return;
    }
    fakeMatrix[y][x] = -1;
}

int **createCopyOfMatrix(int **fakeMatrix) {
    int **copy;
    if (!(copy = malloc(sizeof(int *) * rows))) {
        fprintf(stderr, alloc_error, __LINE__);
        return NULL;
    }
    for (int i = 0; i < rows; ++i) {
        if (!(copy[i] = malloc(sizeof(int) * columns))) {
            fprintf(stderr, alloc_error, __LINE__);
        }
        for (int j = 0; j < columns; ++j) {
            copy[i][j] = fakeMatrix[i][j];
        }
    }
    return copy;
}

bool canBuildAHouse(int **fakeMatrix, int i, int j, int patternType) {
    int height = patterns[(patternType * ELEMENTS_IN_ONE_PATTERN)];
    int width = patterns[(patternType * ELEMENTS_IN_ONE_PATTERN) + 1];
    if (i + height > rows || j + width > columns || !patterns[(patternType * ELEMENTS_IN_ONE_PATTERN) + 2]) {
        return false;
    }
    for (int y = i; y < i + height; ++y) {
        for (int x = j; x < j + width; ++x) {
            if (fakeMatrix[y][x] <= 0) {
                return false;
            }
        }
    }
    return true;
}

void undo(int **fakeMatrix, int **copyOfMatrix, int i, int j, int patternType) {
    int height = patterns[(patternType * ELEMENTS_IN_ONE_PATTERN)];
    int width = patterns[(patternType * ELEMENTS_IN_ONE_PATTERN) + 1];
    if (i + height < rows) {
        height++;
    }
    if (j + width < columns) {
        width++;
    }
    if (i > 0) {
        i--;
        height++;
    }
    if (j > 0) {
        j--;
        width++;
    }
    for (int y = i; y < i + height; ++y) {
        for (int x = j; x < j + width; ++x) {
            fakeMatrix[y][x] = copyOfMatrix[y][x];
        }
    }
}
