#include <functional>
#include "bst_tree.h"
#include <mutex>

void bst_tree::insert(long long data) {
    node * new_node = new node(data);
    // Naimplementujte zde vlaknove-bezpecne vlozeni do binarniho vyhledavaciho stromu
    std::mutex mutex;
    std::unique_lock<std::mutex> ul(mutex);
    if (root == nullptr) {
        this->root = new_node;
        ul.unlock();
        return;
    }
//    ul.unlock();
    node *curr = root;
    while(true) {
        node *next = data > curr->data ? curr->right : curr->left;
        if (next == nullptr) {
            if (data > curr->data) {
                if (curr->right.compare_exchange_strong(next, new_node)) {
                    return;
                }
            } else {
                if (curr->left.compare_exchange_strong(next, new_node)) {
                    return;
                }
            }
        }
        if (data > curr->data) {
            curr = curr->right;
        } else {
            curr = curr->left;
        }
    }
}

bst_tree::~bst_tree() {
    // Rekurzivni funkce pro pruchod stromu a dealokaci pameti prirazene jednotlivym
    // uzlum
    std::function<void(node*)> cleanup = [&](node * n) {
        if(n != nullptr) {
            cleanup(n->left);
            cleanup(n->right);

            delete n;
        }
    };
    cleanup(root);
}
