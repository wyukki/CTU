const Routes = {
    home: {name: 'home', path: '/'},
    categories: {name: 'categories', path: '/categories'},
    category: {name: 'category', path: '/categories/:categoryId'},
    createCategory: {name: 'createCategory', path: '/categories/create'},
    product: {name: 'product', path: '/products/:productId'},
    createProduct: {name: 'createProduct', path: '/products/create'},
    cart: {name: 'cart', path: '/cart'},
    order: {name: 'order', path: '/order/:orderId'}
};

export default Routes;
