#include <stdio.h>

/**
    Simple program, which takes 2 numbers from user and computing some basic mathematical operations.
    Messages are in czech due to message checking in evaluator.
**/
int main() {
    int x;
    int y;
    // numbers must be in intervall (-10000; 10000)
    scanf("%i%i", &x, &y); // input from user

    if (x < -10000 || x > 10000 || y < -10000 || y > 10000) {
        printf("Vstup je mimo interval!\n"); // Means: input is not in interval
        return 0;
    }
    printf("Desitkova soustava: %i %i\n", x, y); // prints numbers in decimal
    printf("Sestnactkova soustava: %x %x\n", x, y); // prints numbers in hex
    printf("Soucet: %i + %i = %i\n", x, y, x + y); // sum of numbers
    printf("Rozdil: %i - %i = %i\n", x, y, x - y); // difference of numbers
    printf("Soucin: %i * %i = %i\n", x, y, x * y); // multiplication of numbers
    if (y != 0) {
        printf("Podil: %i / %i = %i\n", x, y, x / y); // if y != 0 prints division of numbers
    } else {
        printf("Nedefinovany vysledek!\n"); // else prints error message: Not defined result!
    }
    printf("Prumer: %0.1f\n", ((float)(x + y) / 2)); // prints mean of numbers 
    return 0;
}
