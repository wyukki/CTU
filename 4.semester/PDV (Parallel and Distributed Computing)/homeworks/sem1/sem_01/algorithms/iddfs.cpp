#include "iddfs.h"
#include <atomic>
#include <limits.h>
// Naimplementujte efektivni algoritmus pro nalezeni nejkratsi (respektive nej-
// levnejsi) cesty v grafu. V teto metode mate ze ukol naimplementovat pametove
// efektivni algoritmus pro prohledavani velkeho stavoveho prostoru. Pocitejte
// s tim, ze Vami navrzeny algoritmus muze bezet na stroji s omezenym mnozstvim
// pameti (radove nizke stovky megabytu). Vhodnym pristupem tak muze byt napr.
// iterative-deepening depth-first search.
//
// Metoda ma za ukol vratit ukazatel na cilovy stav, ktery je dosazitelny pomoci
// nejkratsi/nejlevnejsi cesty.
std::shared_ptr<const state> best = nullptr;
std::vector<std::shared_ptr<const state>> goals;

void dls(int limit, std::shared_ptr<const state> &curr_node) {
    if (limit > 0) {
        auto neighbours = curr_node->next_states();
        auto neighbours_size = neighbours.size();
        for (unsigned i = 0; i < neighbours_size; ++i) {
            {
#pragma omp task
                {
                    auto curr_neighbour = neighbours[i];
                    if ((curr_node->get_predecessor() == nullptr) ||
                        (curr_node->get_predecessor()->get_identifier() != curr_neighbour->get_identifier())) {
                        dls(limit - 1, curr_neighbour);
                    }
                };
            }
        }
    }
    if (curr_node->is_goal()) {
#pragma omp critical
        {
            if(!best || best->get_identifier() > curr_node->get_identifier() ) {
                best = curr_node;
            }
        };
    }
}

std::shared_ptr<const state> iddfs(std::shared_ptr<const state> root) {
    int limit = 1;
    while (!best) {
#pragma omp parallel
#pragma omp single
        dls(limit, root);
        limit++;
    }
    return best;
}