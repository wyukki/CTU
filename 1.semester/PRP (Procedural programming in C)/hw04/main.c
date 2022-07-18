#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
enum {
    ERROR_WRONG_INPUT = 100,
    MAX_PRIME_NUMBER = 1000000,
    COMPUTE_SIEVE_BORDER = 100000000,
};
/**
 * Reads and returns a number from user.
 */
long int readNumber(void);
/**
 * Creates an array of prime numbers from 0 to maximal prime number (1000000 / 2)
 */
int isNumberPrime(int max_size, int prime_numbers[max_size]);
/**
 * Decomposition of a number with a simple algorithm
 */
void decompose(int number);
/**
 * Decompostion of a number with a sieve of Eratosthenes. Used for big numbers.
 */
void decomposeSieve(long int number, int npn, int primeNumbers[npn]);

/**
 * Programm, which takes a numbers from user, and checks 
 * if a number is prime or computive using a sieve of Eratosthenes.
 * If number is prime, programm prints itself, else prints it's decompostion using only prime numbers
 */
int main(int argc, char *argv[]) {
    long int number;
    int ret = EXIT_SUCCESS;
    int max_prime_numbers = MAX_PRIME_NUMBER / 2;
    int prime_numbers[max_prime_numbers];
    int number_of_prime_numbers = isNumberPrime(max_prime_numbers, prime_numbers);
    while ((number = readNumber()) > 0) {
        printf("Prvociselny rozklad cisla %ld je:\n", number);
        if (number == 1) {
            printf("1\n");
        } else {
            if (number >= COMPUTE_SIEVE_BORDER) {
                decomposeSieve(number, number_of_prime_numbers, prime_numbers);
            } else {
                decompose(number);
            }
            printf("\n");
        }
    }
    if (number < 0) {
        fprintf(stderr, "Error: Chybny vstup!\n");
        ret = ERROR_WRONG_INPUT;
    }

    return ret;
}

long int readNumber(void) {
    long int number = -1;
    if (scanf("%ld", &number) != 1) {
        number = -1;
    }
    return number;
}

void decompose(int number) {
    double number_sqrt = sqrt(number);
    bool first = false;
    int power_counter;
    for (int i = 2; i <= number_sqrt; ++i) {
        power_counter = 0;
        while (number % i == 0) { // computes how many times a number can be divided by a number from 2 to it's square root 
            number = number / i;
            power_counter += 1;
        }
        if (power_counter > 0) {
            if (first) {
                printf(" x ");
            }
            if (power_counter > 1) {
                printf("%d^%d", i, power_counter); // e.g. if a number can be divided by two five times, prints 2^5
            } else {
                printf("%d", i); // else prints a number itself, e.g. 2 from previous example
            }
            first = true;
        }
    }
    if (number > 1) {
        if (first) {
            printf(" x ");
        }
        printf("%d", number);
    }
}

void decomposeSieve(long int number, int number_of_prime_numbers, int prime_numbers[number_of_prime_numbers]) {
    bool first = false;
    int power_counter;
    for (int i = 0; i < number_of_prime_numbers; ++i) {
        power_counter = 0;
        while (number % prime_numbers[i] == 0) { // computes how many times a number can by divided by another number from 2 to 1000000
                                                // using  an array of prime numbers
            number = number / prime_numbers[i];
            power_counter += 1;
        }
        if (power_counter > 0) {
            if (first) {
                printf(" x ");
            }
            if (power_counter > 1) {
                printf("%d^%d", prime_numbers[i], power_counter);
            } else {
                printf("%d", prime_numbers[i]);
            }
            first = true;
            break;
        }
    }
    if (number > 1) {
        if (first) {
            printf(" x ");
        }
        printf("%ld", number);
    }
}

int isNumberPrime(int max_size, int prime_numbers[max_size]) {
    int number_of_prime_numbers = 0;
    bool sieve[MAX_PRIME_NUMBER] = {false};
    for (int i = 2; i < MAX_PRIME_NUMBER; ++i)  // false is prime number, true is computive number
    {
        if (sieve[i] == false) {
            prime_numbers[number_of_prime_numbers] = i;
            number_of_prime_numbers += 1;

            for (int j = i; j < MAX_PRIME_NUMBER; ++j) {
                if ((i * j) < MAX_PRIME_NUMBER) {
                    sieve[i * j] = true;
                } else {
                    break;
                }
            }
        }
    }
    return number_of_prime_numbers;
}
