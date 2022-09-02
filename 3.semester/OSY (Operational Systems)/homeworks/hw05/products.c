#include "products.h"

product_t *createProduct(int type) {
    product_t *product = (product_t *)malloc(sizeof(product_t));
    if (!product) {
        fprintf(stderr, "Allocation error on line %d in file %s!\n", __LINE__, __FILE__);
        return NULL;
    }
    product->step = 0;
    switch (type) {
        case 0:
            product->steps = productA;
            break;
        case 1:
            product->steps = productB;
            break;
        case 2:
            product->steps = productC;
            break;
    }
    return product;
}
