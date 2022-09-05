#include "bfs.h"
#include <map>
#include <vector>
#include <atomic>
#include <limits.h>
#include <unordered_set>

std::shared_ptr<const state> bfs(std::shared_ptr<const state> root) {
    if (root->is_goal()) return root;
    std::vector<std::shared_ptr<const state>> queue;
    queue.push_back(root);
    std::unordered_set<unsigned long long> opened_set;
    std::map<unsigned long long, bool> opened_map;
    std::vector<std::shared_ptr<const state>> goals;
    std::atomic<bool> finish(false);

    while (!queue.empty() && !finish) {
        std::vector<std::shared_ptr<const state>> new_states;
        auto queue_size = queue.size();

#pragma omp parallel for
        for (unsigned i = 0; i < queue_size; ++i) {
            std::shared_ptr<const state> curr_state = queue[i];
            auto neighbours = curr_state->next_states();
            auto neighbours_size = neighbours.size();

            for (unsigned j = 0; j < neighbours_size; ++j) {
                auto curr_neighbour = neighbours[j];

                if (curr_neighbour->is_goal()) {
#pragma omp critical
                    {
                        goals.push_back(curr_neighbour);
                    };
                    finish = true;
                }
                if (!finish) {
#pragma omp critical
                    {
                        if (opened_set.find(curr_neighbour->get_identifier()) == opened_set.end()) {
                            new_states.push_back(curr_neighbour);
                            opened_set.insert(curr_neighbour->get_identifier());
                        }
                    };
                }

            }
        }
        if (finish) {
            break;
        }
        queue.swap(new_states);
    }

    if (goals.size() == 1) {
        return goals[0];
    }
    unsigned long long min_id = ULONG_LONG_MAX;
    std::shared_ptr<const state> min_state = nullptr;
    for (std::shared_ptr<const state> &goal: goals) {
        if (goal->get_identifier() < min_id) {
            min_id = goal->get_identifier();
            min_state = goal;
        }
    }
    return min_state;
}

