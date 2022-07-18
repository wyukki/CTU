#include <stdio.h>
#include <stdlib.h>

#define ERROR_INPUT 100
#define ALLOCATION_ERROR 101

const char *const error_input = "Error: Chybny vstup!\n";

char get_operation(void);
int get_rows_columns(void);
int **read_matrix(int rows, int columns); // returns matrix from input
int print_matrix(int **matrix, int rows, int columns);
int get_multiplication(int matrix1_coord, int matrix2_coord);
int **sum(int **matrix1, int **matrix2, int rows, int columns);
int **sub(int **matrix1, int **matrix2, int rows, int columns);
int **mult(int **matrix1, int **matrix2, int row, int clmn, int rows2);

/**
 * HW06 - Matrices computing.
 * Program gets two matrices from user and an operation, and computing the result, which is displayed to stdout.
 */

int main(int argv, char *argc[]) {
    char operation;
    int rows1, columns1, rows2, columns2;
    int ret = EXIT_SUCCESS;
    int **matrix1, **matrix2, **fin_matrix;
    matrix1 = matrix2 = fin_matrix = NULL;

    rows1 = get_rows_columns(); // number of rows in first matrix
    columns1 = get_rows_columns(); // number of columns in first matrix

    matrix1 = read_matrix(rows1, columns1); // first matrix

    operation = get_operation(); // operation to perform

    rows2 = get_rows_columns(); // number of rows in second matrix
    columns2 = get_rows_columns(); // number of columns in second matrix

    matrix2 = read_matrix(rows2, columns2); // second matrix

    if (operation == '+') {
        if (rows1 == rows2 && columns1 == columns2) { // matrices should have same dimensions
            fin_matrix = sum(matrix1, matrix2, rows1, columns1);
            print_matrix(fin_matrix, rows1, columns2);
        } else {
            fprintf(stderr, error_input);
            ret = ERROR_INPUT;
        }
    } else if (operation == '-') {
        if (rows1 == rows2 && columns1 == columns2) { // matrices should have same dimensions
            fin_matrix = sub(matrix1, matrix2, rows1, columns2);
            print_matrix(fin_matrix, rows1, columns2);
        } else {
            fprintf(stderr, error_input);
            ret = ERROR_INPUT;
        }
    } else {
        if (columns1 == rows2) {
            fin_matrix = mult(matrix1, matrix2, rows1, columns2, rows2);
            print_matrix(fin_matrix, rows1, columns2);
        } else {
            fprintf(stderr, error_input);
            ret = ERROR_INPUT;
        }
    }

    for (int i = 0; i < rows1; i++) {
        free(matrix1[i]);
    }
    for (int i = 0; i < rows2; ++i) {
        free(matrix2[i]);
    }
    if (ret == EXIT_SUCCESS) {
        for (int i = 0; i < rows1; ++i) {
            free(fin_matrix[i]);
        }
        free(fin_matrix);
    }
    free(matrix1);
    free(matrix2);
    return ret;
}

char get_operation(void) {
    char operation;
    int ret = EXIT_SUCCESS;
    scanf(" %c", &operation);

    if (operation != '+' && operation != '-' && operation != '*') {
        fprintf(stderr, error_input);
        ret = ERROR_INPUT;
        return ret;
    }
    return operation;
}

int get_rows_columns(void) {
    int quantity, ret;
    if ((ret = scanf("%d", &quantity) != 1)) {
        fprintf(stderr, error_input);
        exit(ERROR_INPUT);
    } else if (quantity <= 0) {
        fprintf(stderr, error_input);
        exit(ERROR_INPUT);
    }
    return quantity;
}

int **read_matrix(int rows, int columns) {
    int **matrix = NULL;
    int element = 0;
    int ret = 0;
    matrix = malloc(sizeof(int *) * rows);
    if (!matrix) {
        fprintf(stderr, "Allocation error!\n");
        free(matrix);
        exit(ALLOCATION_ERROR);
    }
    for (int i = 0; i < rows; ++i) {
        matrix[i] = malloc(sizeof(int) * columns);

        if (!matrix[i]) {
            fprintf(stderr, "Allovation error!\n");
            free(matrix[i]);
            exit(ALLOCATION_ERROR);
        }
        for (int j = 0; j < columns; ++j) {
            if ((ret = scanf("%d", &element)) != 1) {
                fprintf(stderr, error_input);
                for (int k = 0; k < rows; ++k) {
                    free(matrix[k]);
                }
                free(matrix);
                exit(ERROR_INPUT);
            }
            matrix[i][j] = element;
        }
    }
    return matrix;
}

int print_matrix(int **matrix, int rows, int columns) {
    printf("%d %d\n", rows, columns);
    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < columns; ++j) {
            printf("%d", matrix[i][j]);
            if (j != columns - 1) {
                printf(" ");
            }
        }
        printf("\n");
    }
    return 0;
}

int **sum(int **matrix1, int **matrix2, int rows, int columns) {
    int **matrix = malloc(sizeof(int) * rows * columns);
    if (!matrix) {
        fprintf(stderr, "Allocation error!\n");
        free(matrix);
        exit(ALLOCATION_ERROR);
    }
    for (int i = 0; i < rows; ++i) {
        matrix[i] = malloc(sizeof(int) * columns);
        if (!matrix[i]) {
            fprintf(stderr, "Allocation error!\n");
            free(matrix[i]);
            exit(ALLOCATION_ERROR);
        }
        for (int j = 0; j < columns; ++j) {
            matrix[i][j] = matrix1[i][j] + matrix2[i][j];
        }
    }
    return matrix;
}

int **sub(int **matrix1, int **matrix2, int rows, int columns) {
    int **matrix = malloc(sizeof(int) * rows * columns);
    if (!matrix) {
        fprintf(stderr, "Allocation error!\n");
        free(matrix);
        exit(ALLOCATION_ERROR);
    }
    for (int i = 0; i < rows; ++i) {
        matrix[i] = malloc(sizeof(int) * columns);
        if (!matrix[i]) {
            fprintf(stderr, "Allocation error!\n");
            free(matrix[i]);
            exit(ALLOCATION_ERROR);
        }
        for (int j = 0; j < columns; ++j) {
            matrix[i][j] = matrix1[i][j] - matrix2[i][j];
        }
    }
    return matrix;
}

int get_multiplication(int matrix1_coord, int matrix2_coord) {
    int number = 0;

    number = matrix1_coord * matrix2_coord;

    return number;
}

int **mult(int **matrix1, int **matrix2, int row, int clmn, int rows2) {
    int number = 0;
    int index = 0;
    int **matrix = malloc(sizeof(int *) * row);
    if (!matrix) {
        fprintf(stderr, "Allocation error!\n");
        free(matrix);
        exit(ALLOCATION_ERROR);
    }
    for (int i = 0; i < row; ++i) {
        matrix[i] = malloc(sizeof(int) * clmn);
        if (!matrix[i]) {
            fprintf(stderr, "Allocation error!\n");
            free(matrix);
            exit(ALLOCATION_ERROR);
        }
        for (int j = 0; j < clmn; ++j) {
            while (index < rows2) {
                number += get_multiplication(matrix1[i][index], matrix2[index][j]);
                index++;
            }
            if (index == rows2) {
                index = 0;
            }

            matrix[i][j] = number;
            number = 0;
        }
    }
    return matrix;
}
