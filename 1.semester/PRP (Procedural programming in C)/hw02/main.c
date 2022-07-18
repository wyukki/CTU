#include <stdio.h>

/**
 * Program which takes input from user (numbers in interval (-10000; 10000)
 *  and computes min\max number, mean and percentage of numbers)
 * Messages are in czech due to evaluator.
 */
int main() {
    int x, r;
    int number_counter = 0;
    int number_of_positive = 0, number_of_negative = 0;
    double percentage_of_positive, percentage_of_negative;
    int number_of_even = 0, number_of_odd = 0;
    double percentage_of_positive, percentage_of_negative;
    double mean;
    int sum = 0;
    int min = 10000, max = -10000;

    while ((r = scanf("%i", &x)) > 0) {
        if (x < -10000 || x > 10000) {
            printf("\nError: Vstup je mimo interval!\n");  // error: Input is not in interval
            return 100;
        }

        if (number_counter == 0) {
            printf("%d", x);
        } else {
            printf(", %d", x);
        }
        number_counter += 1;
        sum += x;
        mean = ((double)sum / number_counter);

        if (x > max) {
            max = x;
        }
        if (x < min) {
            min = x;
        }

        if (x > 0) {
            number_of_positive += 1;
        } else {
            number_of_negative += 1;
        }

        if (x % 2 == 0) {
            number_of_even += 1;
        } else {
            number_of_odd += 1;
        }

        percentage_of_positive = (double)number_of_positive / number_counter * 100.0;
        percentage_of_negative = (double)number_of_negative / number_counter * 100.0;

        percentage_of_positive = (double)number_of_even / number_counter * 100.0;
        percentage_of_negative = (double)number_of_odd / number_counter * 100.0;
    }

    if (r == EOF) {
        if (x >= -10000 && x <= 10000) {
            printf("\nPocet cisel: %i\n", number_counter);                 // prints counter of numbers
            printf("Pocet kladnych: %i\n", number_of_positive);            // prints number of positive numbers
            printf("Pocet zapornych: %i\n", number_of_negative);           // prints number of negative numbers
            printf("Procento kladnych: %.2f\n", percentage_of_positive);   // prints percentage of positive numbers
            printf("Procento zapornych: %.2f\n", percentage_of_negative);  // prints percentage of negative numbers
            printf("Pocet sudych: %i\n", number_of_even);                  // prints number of even numbers
            printf("Pocet lichych: %i\n", number_of_odd);                  // prints number of odd numbers
            printf("Procento sudych: %.2f\n", percentage_of_positive);     // prints percentage of positive numbers
            printf("Procento lichych: %.2f\n", percentage_of_negative);    // prints percentage of negative numbers
            printf("Mean: %.2f\n", mean);                                  // prints mean of numbers
            printf("Maximum: %i\n", max);                                  // prints maximum number
            printf("Minimum: %i\n", min);                                  // prints minimal number
        }
    }
    return 0;
}
