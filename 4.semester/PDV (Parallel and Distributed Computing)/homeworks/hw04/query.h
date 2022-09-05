#ifndef DATABASEQUERIES_QUERY_H
#define DATABASEQUERIES_QUERY_H

#include <vector>
#include <functional>
#include <atomic>
#include <mutex>

template<typename row_t>
using predicate_t = std::function<bool(const row_t &)>;


template<typename row_t>
bool is_satisfied_for_all(std::vector<predicate_t<row_t>> predicates, std::vector<row_t> data_table);

template<typename row_t>
bool is_satisfied_for_any(std::vector<predicate_t<row_t>> predicates, std::vector<row_t> data_table);


template<typename row_t>
bool is_satisfied_for_all(std::vector<predicate_t<row_t>> predicates, std::vector<row_t> data_table) {
    unsigned int pred_len = predicates.size();
    unsigned int row_count = data_table.size();
    std::atomic<bool> satisfied(true);
#pragma omp parallel for default(none) shared(satisfied, pred_len, predicates, row_count, data_table)
    for (unsigned int i = 0; i < pred_len; ++i) {
        if (!satisfied) {
            i = pred_len;
            continue;
        }
        auto &pred = predicates[i];
        bool loc_ret = false;
        for (unsigned int j = 0; j < row_count; ++j) {
            auto & row = data_table[j];
            bool is_satisfied = pred(row);
            if (is_satisfied) {
                loc_ret = true;
                break;
            }
        }
        if (!loc_ret) {
            i = pred_len;
            satisfied = false;
            continue;
        }
    }
    return satisfied;
}

template<typename row_t>
bool is_satisfied_for_any(std::vector<predicate_t<row_t>> predicates, std::vector<row_t> data_table) {
    // Doimplementujte telo funkce, ktera rozhodne, zda je ALESPON JEDEN dilci dotaz pravdivy.
    // To znamena, ze mate zjistit, zda existuje alespon jeden predikat 'p' a jeden zaznam
    // v tabulce 'r' takovy, ze p(r) vraci true.

    // Zamyslete se nad tim, pro ktery druh dotazu je vhodny jaky druh paralelizace. Vas
    // kod optimalizujte na situaci, kdy si myslite, ze navratova hodnota funkce bude true.
    // Je pro Vas dulezitejsi rychle najit splnujici radek pro jeden vybrany predikat, nebo
    // je dulezitejsi zkouset najit takovy radek pro vice predikatu paralelne?
    unsigned int pred_len = predicates.size();
    unsigned int row_count = data_table.size();
    for (unsigned int i = 0; i < pred_len; ++i) {
        std::atomic<bool> not_satisfied(true);
        auto &pred = predicates[i];
#pragma omp parallel for default(none) shared(not_satisfied, row_count, pred, data_table)
        for (unsigned int j = 0; j < row_count; ++j) {
            if (!not_satisfied) {
                j = row_count;
                continue;
            }
            auto & row = data_table[j];
            bool is_satisfied = pred(row);
            if (is_satisfied) {
                not_satisfied = false;
                j = row_count;
            }
        }
        if (!not_satisfied) {
            return true;
        }
    }
    return false;
}


#endif //DATABASEQUERIES_QUERY_H
