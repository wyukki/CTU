#include "sort.h"
#include <iostream>
#include "omp.h"

// implementace vaseho radiciho algoritmu. Detalnejsi popis zadani najdete v "sort.h"
void print_strings(std::vector<std::string *> &vector_to_sort) {
    for (std::string *s: vector_to_sort) {
        for (char c: *s) {
            printf("%c", c);
        }
        printf("\n");
    }
}


void bucket_sort(std::vector<std::string *> &vector_to_sort, unsigned long alphabet_size,
                 const MappingFunction &mappingFunction, unsigned long char_index,
                 unsigned long strings_length) {
    unsigned long size = vector_to_sort.size();
    if (size == 1 || char_index == strings_length) {
        return;
    }
    std::vector <std::vector<std::string *>> buckets(alphabet_size);
    for (unsigned long i = 0; i < size; ++i) {
        std::string *s = vector_to_sort[i];
        buckets[mappingFunction(s->at(char_index))].push_back(s);
    }
#pragma omp parallel for
    for (unsigned long i = 0; i < alphabet_size; ++i) {
#pragma omp task
        bucket_sort(buckets[i], alphabet_size, mappingFunction, char_index + 1, strings_length);
    }
#pragma omp taskwait
    int index = 0;
    for (unsigned long i = 0; i < alphabet_size; ++i) {
        for (std::string *s: buckets[i]) {
            vector_to_sort[index++] = s;
        }
    }
}


std::vector<std::string *> sort_one_char(const std::vector<std::string *> &strings, unsigned long char_index,
                                         unsigned long alphabet_length) {

    unsigned long size = strings.size();
    std::vector < std::string * > bucket(size);
    int counts[alphabet_length];
    for (unsigned long i = 0; i < alphabet_length; ++i) {
        counts[i] = 0;
    }
    for (std::string *s: strings) {
        counts[s->at(char_index) % 65]++;
    }

    int indexes[alphabet_length];

    for (unsigned i = 0; i < alphabet_length; ++i) {
        if (i == 0) {
            indexes[i] = counts[0];
        } else {
            indexes[i] = counts[i] + indexes[i - 1];
        }
    }

    for (std::string *s: strings) {
        char c = s->at(char_index); // last char in string
        int index = c % 65;
        bucket[indexes[index] - counts[index]] = s;
        counts[index]--;
    }
    return bucket;
}

std::vector<std::string *> bucketSort(std::vector<std::string *> &vector_to_sort, unsigned long alphabet_size,
                                      const MappingFunction &mappingFunction) {
    if (vector_to_sort.size() == 1) {
        return vector_to_sort;
    }
    std::vector <std::vector<std::string * >> buckets(alphabet_size);

    for (std::string *s: vector_to_sort) {
        buckets[mappingFunction(s->at(0))].push_back(s);
    }

    for (unsigned long i = 0; i < alphabet_size; ++i) {
        bucketSort(buckets[i], alphabet_size, mappingFunction);
    }

    std::vector < std::string * > res(vector_to_sort.size());
    for (unsigned long i = 0; i < alphabet_size; ++i) {
        for (std::string *s: buckets[i]) {
            res.push_back(s);
        }
    }
    return res;
}

void radix_par(std::vector<std::string *> &vector_to_sort, const MappingFunction &mappingFunction,
               unsigned long alphabet_size, unsigned long string_lengths) {
    // sem prijde vase implementace. zakomentujte tuto radku
    // abeceda se nemeni. jednotlive buckety by mely reprezentovat znaky teto abecedy. poradi znaku v abecede
    // dostanete volanim funkce mappingFunction nasledovne: mappingFunction((*p_retezec).at(poradi_znaku))

    // vytvorte si spravnou reprezentaci bucketu, kam budete retezce umistovat

    // pro vetsi jednoduchost uvazujte, ze vsechny retezce maji stejnou delku - string_lengths. nemusite tedy resit
    // zadne krajni pripady

    // na konci metody by melo byt zaruceno, ze vector pointeru - vector_to_sort bude spravne serazeny.
    // pointery budou serazeny podle retezcu, na ktere odkazuji, kdy retezcu jsou serazeny abecedne
    bucket_sort(vector_to_sort, alphabet_size, mappingFunction, 0L, string_lengths);
}