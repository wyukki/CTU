#include <stdio.h>
#include <stdlib.h>

#define TABLE(i,j) table[i][j]

int main() {
	int **table = (int **)malloc(sizeof(int *) * 10);
	for (int i = 0; i < 3; ++i) {
		table[i] = malloc(sizeof(int) * 10);
		for (int j = 0; j < 10; ++j) {
			table[i][j] = j;
		}
	}
	for (int i = 0; i < 3; ++i) {
		for (int j = 0; j < 10; ++j) {
			printf("table[%d][%d] = %d\n", i,j, TABLE(i, j));
		}
	}
	return 0;
}

