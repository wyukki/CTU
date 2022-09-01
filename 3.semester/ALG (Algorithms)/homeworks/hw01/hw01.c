#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#define ARGUMENTS_IN_ARRAY 4

const char* const alloc_error = "Allocation error on line %d\n!";

char** getMatrix(int rows, int columns);
void printVerticals(int* verticals, int verticalsLength);
int findLeftAndRightPart(int i, int j, int length, int rows, int columns, bool oInRow);
int compareIndexies(int leftUp, int rightUp, int leftDown, int rightDown);

int* verticals; // global because it takes a huge amount of memory, and can't be parsed like an argument

/**
 * The problem is to place 3 lines on the field the way they'll take
 * the biggest count of squares. There are some rules: figure, that we
 * should place, must contain 2 vertical and 1 lines. Sizes of vertical lines
 * should be the same, horizontal line cannot be placed on the top\bottom of the
 * vertical lines. 
 * 
 * There are 3 kinds of squares: blue, yellow and red.
 * Blue square means, that the territory is good; yellow means, that territory
 * is half-useful (we can have only one yellow square in whole figure);
 * and red square means, that we cannot place our figure on that place.
 * 
 * There is an image in this directory, that would describe problem better.
 * 10 points out of 10.
 */

int main(int argc, char* argv[]) {
    int ret = EXIT_SUCCESS;
    int rows, columns;
    int max = 0;
    if ((scanf("%d %d\n", &rows, &columns)) != 2) {
        fprintf(stderr, "Error: wrong input!\n");
        return -1;
    }
    char** matrix = getMatrix(rows, columns);
    verticals = malloc(sizeof(int) * ARGUMENTS_IN_ARRAY * rows * columns);
    int verticalsLength = 0;
    if (!verticals) {
        fprintf(stderr, alloc_error, __LINE__);
        return -1;
    }
    for (int j = 0; j < columns; ++j) {
        for (int i = 0; i < rows; ++i) {
            int clearLengthUp = 0;
            int clearLengthDown = 0;
            int dirtyLengthUp = 0;
            int dirtyLengthDown = 0;
            int oCounter = 0;
            if (matrix[i][j] != 'X') {
                if (i != 0) {
                    for (int k = i - 1; k >= 0; --k) {
                        char c = matrix[k][j];
                        if (c == 'X') {
                            break;
                        } else if (c == 'o') {
                            if (oCounter == 1) {
                                oCounter = 0;
                                break;
                            }
                            oCounter++;
                            dirtyLengthUp++;
                        } else if (oCounter > 0) {
                            dirtyLengthUp++;
                        } else {
                            if (k != i) {
                                clearLengthUp++;
                            }
                        }
                    }
                }

                dirtyLengthUp += clearLengthUp;
                oCounter = 0;
                for (int k = i + 1; k < rows; ++k) {
                    char c = matrix[k][j];
                    if (c == 'X') {
                        break;
                    } else if (c == 'o') {
                        if (oCounter == 1) {
                            break;
                        }
                        oCounter++;
                        dirtyLengthDown++;
                    } else if (oCounter > 0) {
                        dirtyLengthDown++;
                    } else {
                        clearLengthDown++;
                    }
                }
                dirtyLengthDown += clearLengthDown;
            }
            verticals[verticalsLength] = clearLengthUp;
            verticals[verticalsLength + 1] = clearLengthDown;
            verticals[verticalsLength + 2] = dirtyLengthUp;
            verticals[verticalsLength + 3] = dirtyLengthDown;
            verticalsLength += 4;
        }
    }
    for (int i = 1; i < rows - 1; ++i) {
        for (int j = 1; j < columns - 1; ++j) {
            int dirtyLen = 0;
            int clearLen = 0;
            bool oInRow = false;
            for (int k = j; k < columns; ++k) {
                char c = matrix[i][k];
                if (c == 'X') {
                    break;
                } else if (c == 'o') {
                    if (oInRow) {
                        break;
                    }
                    oInRow = true;
                    dirtyLen++;
                } else if (oInRow) {
                    dirtyLen++;
                } else {
                    clearLen++;
                }
            }
            dirtyLen += clearLen;
            for (int k = 2; k <= dirtyLen; ++k) {
                if (k <= clearLen) {
                    int r = findLeftAndRightPart(i, j, k, rows, columns, false);
                    if (max < r) {
                        max = r;
                    }
                } else {
                    int r = findLeftAndRightPart(i, j, k, rows, columns, oInRow);
                    if (max < r) {
                        max = r;
                    }
                }
            }
        }
    }
    printf("%d\n", max);
    free(verticals);
    for (int i = 0; i < rows; ++i) {
        free(matrix[i]);
    }
    free(matrix);
    return ret;
}

char** getMatrix(int rows, int columns) {
    char** matrix = malloc(sizeof(char*) * rows);
    if (!matrix) {
        fprintf(stderr, alloc_error, __LINE__);
        return NULL;
    }
    for (int i = 0; i < rows; ++i) {
        matrix[i] = malloc(columns + 1);
        if (!matrix[i]) {
            fprintf(stderr, alloc_error, __LINE__);
            free(matrix);
            return NULL;
        }
        for (int j = 0; j < columns; ++j) {
            char c;
            int ret = scanf(j == columns - 1 ? "%c\n" : "%c", &c);
            if (ret != 1) {
                fprintf(stderr, "Wrong input!\n");
                return NULL;
            }
            matrix[i][j] = c;
        }
    }
    return matrix;
}

void printVerticals(int* verticals, int verticalsLength) {
    for (int i = 0; i < verticalsLength; i += ARGUMENTS_IN_ARRAY) {
        printf("StartI: %d, column: %d, clearLengthUp: %d, clearLengthDown: %d, dirtyLengthUp: %d, dirtyLengthDown: %d\n",
               verticals[i], verticals[i + 1], verticals[i + 2], verticals[i + 3], verticals[i + 4], verticals[i + 5]);
        printf("-------------------------------------------------------------------------------------------------------\n");
    }
}
int findLeftAndRightPart(int i, int j, int length, int rows, int columns, bool oInRow) {
    int l = (rows * ARGUMENTS_IN_ARRAY) * j + (i * ARGUMENTS_IN_ARRAY);
    int r = (rows * ARGUMENTS_IN_ARRAY) * (j + length - 1) + (i * ARGUMENTS_IN_ARRAY);
    int leftClearUp = verticals[l];
    int leftClearDown = verticals[l + 1];
    int leftDirtyUp = verticals[l + 2];
    int leftDirtyDown = verticals[l + 3];
    int rightClearUp = verticals[r];
    int rightClearDown = verticals[r + 1];
    int rightDirtyUp = verticals[r + 2];
    int rightDirtyDown = verticals[r + 3];
    int ret = 0;
    if (!leftDirtyUp || !leftDirtyDown || !rightDirtyDown || !rightDirtyUp) {
        return 0;
    } else if (oInRow && (!leftClearUp || !rightClearUp || !rightClearDown || !leftClearDown)) {
        return 0;
    }
    if (oInRow) {
        ret = compareIndexies(leftClearUp, rightClearUp, leftClearDown, rightClearDown) + length;
    } else {
        int var1 = compareIndexies(leftDirtyUp, rightClearUp, leftClearDown, rightClearDown) + length;
        int var2 = compareIndexies(leftClearUp, rightDirtyUp, leftClearDown, rightClearDown) + length;
        int var3 = compareIndexies(leftClearUp, rightClearUp, leftDirtyDown, rightClearDown) + length;
        int var4 = compareIndexies(leftClearUp, rightClearUp, leftClearDown, rightDirtyDown) + length;
        int cmp1 = var1 > var2 ? var1 : var2;
        int cmp2 = var3 > var4 ? var3 : var4;
        ret = cmp1 > cmp2 ? cmp1 : cmp2;
    }
    return ret;
}

int compareIndexies(int leftUp, int rightUp, int leftDown, int rightDown) {
    int up;
    int down;
    if (leftUp == 0 || rightUp == 0) {
        return 0;
    } else if (rightDown == 0 || leftDown == 0) {
        return 0;
    }
    if (leftUp == rightUp) {
        up = leftUp;
    } else {
        if (leftUp > rightUp) {
            up = rightUp;
        } else {
            up = leftUp;
        }
    }
    if (leftDown == rightDown) {
        down = leftDown;
    } else {
        if (leftDown > rightDown) {
            down = rightDown;
        } else {
            down = leftDown;
        }
    }
    return (up + down) * 2;
}
