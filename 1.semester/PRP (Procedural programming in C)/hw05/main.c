#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define INIT_SIZE 10

/**
 * 
 */

enum {
    ERROR_INPUT = 100,
    ERROR_LENGTH = 101
};
/**
 * A structure, which holds ininital message and it's decrypted version
 */
typedef struct
{
    char *cipher;
    char *inc_msg;
} msg_t;

const char *const error_str_input = "Error: Chybny vstup!\n"; // Error: Wrong input
const char *const error_str_length = "Error: Chybna delka vstupu!\n"; // Error: Wrong length of input
const char *const alloc_error = "Allocation error!\n";

/**
 * Allocates memory for a message
 */
msg_t *allocate_msg();
/**
 * Frees memory
 */
void free_msg(msg_t *msg);
/**
 * Reads and returns encryprted message from stdin.
 */
char *read_cipher(msg_t *msg);
/**
 * Tests if cipher contains only letters
 */
int test_cipher(msg_t *msg);
/**
 * Tests if overheard message contains only letters 
 */
int test_enc_msg(msg_t *msg);
/**
 * Reads and returns "unreliable overheard" message
 */
char *read_enc_message(msg_t *msg);
/**
 * Compares two messages and return most frequent distance between two letters
 */
int compare(msg_t *msg, int len);
/**
 * Shifts encrypted message with an offset from previous function.
 */
void shift(msg_t *msg, int offset, int len);

/**
 * HW06 - Caesar cipher
 * A programm takes two string from user. First string is encoded message, second is the
 * unreliable overheard message. The task is to decrypt first message using the second one by 
 * comparing each letter in both messages.
 */

int main(int argc, char *argv[]) {
    int ret = 0;
    int ret_cipher, ret_inc_msg;
    int cipher_len, inc_msg_len;
    ret_cipher = ret_inc_msg = 0;
    cipher_len = inc_msg_len = 0;
    msg_t *messages = allocate_msg();

    messages->cipher = read_cipher(messages);
    cipher_len = strlen(messages->cipher);
    ret_cipher = test_cipher(messages);

    messages->inc_msg = read_enc_message(messages);
    inc_msg_len = strlen(messages->inc_msg);
    ret_inc_msg = test_enc_msg(messages);
    int offset = compare(messages, cipher_len);
    if (ret_cipher || ret_inc_msg) {
        ret = ERROR_INPUT;
    } else if (cipher_len != inc_msg_len) {
        fprintf(stderr, error_str_length);
        ret = ERROR_LENGTH;
    }
    if (!ret) {
        shift(messages, offset, cipher_len);
    }
    free_msg(messages);

    return ret;
}
msg_t *allocate_msg() {
    msg_t *msg = malloc(sizeof(msg_t));
    if (!msg) {
        fprintf(stderr, alloc_error);
        free(msg);
        return NULL;
    }
    msg->cipher = NULL;
    msg->inc_msg = NULL;
    return msg;
}

void free_msg(msg_t *msg) {
    free(msg->cipher);
    free(msg->inc_msg);
    free(msg);
}

char *read_cipher(msg_t *msg) {
    int capacity = INIT_SIZE;
    int size = 0;
    msg->cipher = malloc(capacity);
    if (!msg->cipher) {
        fprintf(stderr, alloc_error);
        return NULL;
    }
    char c;
    while ((c = getchar()) != EOF && c != '\n') {
        if (size == capacity) { // reallocation
            char *tmp = malloc(capacity * 2);
            if (!tmp) {
                fprintf(stderr, alloc_error);
                free(msg->cipher);
                return NULL;
            }
            memcpy(tmp, msg->cipher, size);
            free(msg->cipher);
            msg->cipher = tmp;
            capacity *= 2;
        }
        msg->cipher[size] = c;
        size++;
    }
    if (size < capacity) {
        msg->cipher[size] = '\0';
    } else { // reallocation in needed
        char *tmp = malloc(capacity * 2);
        if (!tmp) {
            fprintf(stderr, alloc_error);
            free(msg->cipher);
            return NULL;
        }
        memcpy(tmp, msg->cipher, size);
        free(msg->cipher);
        msg->cipher = tmp;
        msg->cipher[size] = '\0';
    }
    return msg->cipher;
}
char *read_enc_message(msg_t *msg) {
    int capacity = INIT_SIZE;
    int size = 0;
    char c;
    msg->inc_msg = malloc(capacity);
    if (!msg->inc_msg) {
        fprintf(stderr, alloc_error);
        return NULL;
    }
    while ((c = getchar()) != EOF && c != '\n') {
        if (size == capacity) { // reallocation
            char *tmp = malloc(capacity * 2);
            if (!tmp) {
                fprintf(stderr, alloc_error);
                free(msg->inc_msg);
                return NULL;
            }
            memcpy(tmp, msg->inc_msg, size);
            free(msg->inc_msg);
            msg->inc_msg = tmp;
            capacity *= 2;
        }
        msg->inc_msg[size] = c;
        size++;
    }
    if (size < capacity) {
        msg->inc_msg[size] = '\0';
    } else { // reallocation
        char *tmp = malloc(capacity * 2);
        if (!tmp) {
            fprintf(stderr, alloc_error);
            free(msg->inc_msg);
            return NULL;
        }
        memcpy(tmp, msg->inc_msg, size);
        free(msg->inc_msg);
        msg->inc_msg = tmp;
        msg->inc_msg[size] = '\0';
    }
    return msg->inc_msg;
}
int test_cipher(msg_t *msg) {
    int ret = 0;
    int len = strlen(msg->cipher);
    for (int i = 0; i < len; ++i) {
        // if cipher contains not a letter return an error
        if ((msg->cipher[i] < 'A') || (msg->cipher[i] > 'Z' && msg->cipher[i] < 'a') || msg->cipher[i] > 'z') {
            fprintf(stderr, error_str_input);
            ret = -1;
        }
    }
    return ret;
}
int test_enc_msg(msg_t *msg) {
    int ret = 0;
    int len = strlen(msg->inc_msg);
    for (int i = 0; i < len; ++i) {
        // if overheard message contains not a letter return an error
        if ((msg->inc_msg[i] < 'A') || (msg->inc_msg[i] > 'Z' && msg->inc_msg[i] < 'a') || msg->inc_msg[i] > 'z') {
            fprintf(stderr, error_str_input);
            ret = -1;
        }
    }
    return ret;
}
int compare(msg_t *msg, int len) {
    int distance = 0;
    int *arr = malloc(sizeof(int) * len);
    if (!arr) {
        fprintf(stderr, alloc_error);
        return EXIT_FAILURE;
    }
    // for each letter in two messages compute their distance using ASCII table.
    for (int i = 0; i < len; ++i) {
        distance = msg->cipher[i] - msg->inc_msg[i];
        if (distance > 0) {
            // ecrypted message if between 'a' and 'z' and overheard message is between 'A' and 'Z'
            if ((msg->cipher[i] > 96 && msg->cipher[i] < 123) && msg->inc_msg[i] > 64 && msg->inc_msg[i] < 91) {
                distance -= 6;
            }
        }
        if (distance < 0) {
            if (msg->cipher[i] > 96 && msg->cipher[i] < 123) {
                distance += 52;
            } else if ((msg->cipher[i] > 64 && msg->cipher[i] < 91) && msg->inc_msg[i] > 64 && msg->inc_msg[i] < 91) {
                distance += 52;
            } else {
                distance += 52 + 6;
            }
        }
        arr[i] = distance;
    }

    int max_num = arr[0];
    int max_frq = 1;
    // computer most frequent distance in array
    for (int i = 0; i < len - 1; ++i) {
        int frq = 1;
        for (int k = i + 1; k < len; ++k) {
            if (arr[i] == arr[k]) {
                frq += 1;
            }
            if (frq > max_frq) {
                max_frq = frq;
                max_num = arr[i];
            }
        }
    }
    free(arr);
    return max_num;
}

void shift(msg_t *msg, int offset, int len) {
    char *tmp = msg->cipher;
    int temp = 0;
    for (int i = 0; i < len; ++i) {
        if (msg->cipher[i] > 96 && msg->cipher[i] < 123) { // letter between 'a' and 'z'
            if (msg->cipher[i] - offset < 97) { // letter is not small, shift to capital letters
                temp = 97 - (msg->cipher[i] - offset);
                tmp[i] = 91 - temp;
                if (tmp[i] < 65) { // letter in not capital, shift to small letters
                    temp = 65 - tmp[i];
                    tmp[i] = 123 - temp;
                    continue;
                }
            } else {
                tmp[i] = msg->cipher[i] - offset;
                continue;
            }
        } else if (msg->cipher[i] > 64 && msg->cipher[i] < 91) { // letter between 'A' and 'Z'
            if (msg->cipher[i] - offset < 65) { // letter in not capital, shift to small letters
                temp = 65 - (msg->cipher[i] - offset);
                tmp[i] = 123 - temp;
                if (tmp[i] < 97) { // letter is not small, shift to capital letters
                    temp = 97 - tmp[i];
                    tmp[i] = 91 - temp;
                    continue;
                }
            } else {
                tmp[i] = msg->cipher[i] - offset;
                continue;
            }
        }
    }
    printf("%s\n", tmp);
}
