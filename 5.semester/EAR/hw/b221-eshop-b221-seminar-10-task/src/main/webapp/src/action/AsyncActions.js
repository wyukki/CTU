import ActionType from "./ActionType";
import Axios from "axios";
import {
    addToLocalCart,
    asyncActionFailure,
    asyncActionRequest,
    asyncActionSuccess,
    createCategorySuccess,
    loadCategoriesSuccess,
    loadCategoryProductsSuccess,
    loadCategorySuccess,
    loadOrderSuccess,
    loadProductSuccess,
    loadUserSuccess,
    logoutSuccess,
    publishMessage,
    removeFromLocalCart,
} from "./SyncActions";
import Routes from "../util/Routes";
import Routing from "../util/Routing";
import MockAdapter from "axios-mock-adapter";

const axios = Axios.create();

const SERVER_URL = process.env.REACT_APP_SERVER_URL || "";

/**
 * This part allows to mock server REST API so that the frontend can be developed without access to a real backend.
 */
if (process.env.REACT_APP_MOCK_REST_API) {
    // Mock backend REST API if the environment is configured to do so
    const mock = new MockAdapter(axios, {delayResponse: 200});
    // Mock current user data
    mock.onGet('rest/users/current').reply(200, {
        firstName: 'Catherine',
        lastName: 'Halsey',
        username: 'halsey@unsc.org',
        role: 'ADMIN'
    });
    // Mock login return value
    mock.onPost('/j_spring_security_check').reply(200, {
        loggedIn: true,
        success: true
    });
    const categories = [{
        id: '1',
        name: 'Computers'
    }, {
        id: '2',
        name: 'Tablets'
    }, {
        id: '3',
        name: 'Phones'
    }];
    mock.onGet('rest/categories').reply(200, categories);
    mock.onPost('rest/categories').reply(2001, null);
    const products = [{
        id: '1',
        name: 'HP Elite 123',
        amount: 5,
        price: 35000.00,
        categories: categories.slice(0, 2)
    }, {
        id: '2',
        name: 'HP Elite 124',
        amount: 2,
        price: 36000.00
    }, {
        id: '3',
        name: 'HP Elite 543',
        amount: 3,
        price: 30000.00
    }, {
        id: '4',
        name: 'HP Elite 555',
        amount: 10,
        price: 17000.00
    }];
    mock.onGet(/rest\/categories\/\d+\/products/).reply(200, products);
    mock.onGet(/rest\/categories\/\d+/).reply(config => {
        const url = config.url;
        const id = url.substring(url.lastIndexOf('/') + 1);
        return [200, categories[Number(id - 1)]];
    });
    mock.onGet(/rest\/products\/\d+/).reply(config => {
        const url = config.url;
        const id = url.substring(url.lastIndexOf('/') + 1);
        return [200, products[Number(id - 1)]];
    });
    mock.onPost('rest/products').reply(201, null, {
        'location': 'http://localhost:3000/eshop/rest/products/3'
    });
    mock.onPut(/rest\/products\/\d+/).reply(204);
    mock.onDelete(/rest\/products\/\d+/).reply(204);
    mock.onPut('rest/cart/items').reply(204);
    mock.onDelete('rest/cart/items').reply(204);
    mock.onPost('rest/orders').reply(201, null, {location: 'http://localhost:3000/eshop/rest/orders/1'});
    mock.onGet(/rest\/orders\/\d+/).reply(200, {
        id: 1,
        created: "13.09.2018",
        items: [{
            id: 1,
            product: products[0],
            amount: 1
        }, {
            id: 2,
            product: products[2],
            amount: 2
        }]
    });
}

/**
 * These Redux actions work asynchronously because they communicate with the backend. Their results are passed to the
 * Redux store via the dispatch function.
 */


export function loadCategories() {
    const action = {
        type: ActionType.LOAD_CATEGORIES
    };
    return (dispatch) => {
        dispatch(asyncActionRequest(action));
        return axios.get(`${SERVER_URL}rest/categories`)
            .then(resp => dispatch(loadCategoriesSuccess(resp.data)))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    };
}

export function loadCategory(categoryId) {
    const action = {
        type: ActionType.LOAD_CATEGORY
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.get(`${SERVER_URL}rest/categories/${categoryId}`)
            .then(resp => dispatch(loadCategorySuccess(resp.data)))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    };
}

export function loadCategoryProducts(categoryId) {
    const action = {
        type: ActionType.LOAD_CATEGORY_PRODUCTS
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.get(`${SERVER_URL}rest/categories/${categoryId}/products`)
            .then(resp => dispatch(loadCategoryProductsSuccess(resp.data)))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    };
}

export function login(username, password) {
    const action = {
        type: ActionType.LOGIN
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        const params = new URLSearchParams();
        params.append('username', username);
        params.append('password', password);
        return axios.post(`${SERVER_URL}j_spring_security_check`, params)
            .then((resp) => {
                if (resp.data.loggedIn) {
                    dispatch(publishMessage({message: 'Login successful.', type: 'success'}));
                    return dispatch(loadUser());
                } else {
                    dispatch(asyncActionFailure(action, resp.data.message));
                    return dispatch(publishMessage({message: resp.data.message, type: 'danger'}));
                }
            })
            .catch(error => dispatch(asyncActionFailure(action, error.response.data)));
    };
}

export function loadUser() {
    const action = {
        type: ActionType.LOAD_USER
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.get(`${SERVER_URL}rest/users/current`)
            .then(resp => dispatch(loadUserSuccess(resp.data)))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    };
}

export function logout() {
    const action = {
        type: ActionType.LOGOUT
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.post(`${SERVER_URL}j_spring_security_logout`)
            .then(() => dispatch(logoutSuccess()))
            .then(() => {
                Routing.transitionTo(Routes.home);
                window.location.reload();
                return dispatch(publishMessage({message: 'Logout successful.', type: 'success'}));
            })
            .catch(error => dispatch(asyncActionFailure(action, error.response.data)));
    };
}

export function register(profile) {
    const action = {
        type: ActionType.REGISTER
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.post(`${SERVER_URL}rest/users`, profile)
            .then(() => dispatch(asyncActionSuccess(action)))
            .then(() => dispatch(publishMessage({message: 'Registration successful.', type: 'success'})))
            .then(() => dispatch(login(profile.username, profile.password)))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                dispatch(asyncActionFailure(action, error.response.data));
            });
    }
}

export function createCategory(name) {
    const action = {
        type: ActionType.CREATE_CATEGORY
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.post(`${SERVER_URL}rest/categories`, {name})
            .then(() => dispatch(createCategorySuccess()))
            .then(() => Routing.transitionTo(Routes.home))
            .then(() => dispatch(loadCategories()))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                dispatch(asyncActionFailure(action, error.response.data));
            });
    };
}

export function loadProduct(productId) {
    const action = {
        type: ActionType.LOAD_PRODUCT
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.get(`${SERVER_URL}rest/products/${productId}`)
            .then(resp => dispatch(loadProductSuccess(resp.data)))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    };
}

export function createProduct(product) {
    const action = {
        type: ActionType.CREATE_PRODUCT
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.post(`${SERVER_URL}rest/products`, product)
            .then(resp => {
                const id = extractIdFromLocation(resp);
                Routing.transitionTo(Routes.product, {productId: id});
                return dispatch(publishMessage({message: 'Product created.', type: 'success'}));
            })
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    };
}

function extractIdFromLocation(resp) {
    const location = resp.headers['location'];
    return location.substring(location.lastIndexOf('/') + 1);
}

export function updateProduct(product) {
    const action = {
        type: ActionType.UPDATE_PRODUCT
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.put(`${SERVER_URL}rest/products/${product.id}`, product)
            .then(() => dispatch(publishMessage({message: 'Product updated.', type: 'success'})))
            .then(() => dispatch(asyncActionSuccess(action)))
            .then(() => dispatch(loadProduct(product.id)))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    };
}

export function removeProduct(product) {
    const action = {
        type: ActionType.REMOVE_PRODUCT
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.delete(`${SERVER_URL}rest/products/${product.id}`)
            .then(() => dispatch(publishMessage({message: 'Product removed.', type: 'success'})))
            .then(() => dispatch(asyncActionSuccess(action)))
            .then(() => Routing.transitionToHome())
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    }
}

export function addToCart(item, user) {
    const action = addToLocalCart(item);
    if (!user) {
        return dispatch => {
            dispatch(action);
            return dispatch(publishMessage({message: 'Item added into your cart.', type: 'info'}));
        }
    }
    return dispatch => {
        dispatch(action);
        // No loading spinner for this action, let it happen silently in the background
        return axios.put(`${SERVER_URL}rest/cart/items`, item)
            .then(() => dispatch(publishMessage({message: 'Item added into your cart.', type: 'info'})))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    }
}

export function removeFromCart(item, user) {
    const action = removeFromLocalCart(item);
    if (!user) {
        return dispatch => {
            dispatch(action);
            return dispatch(publishMessage({message: 'Item removed from your cart.', type: 'info'}));
        }
    }
    return dispatch => {
        // Now do just the local part
        dispatch(action);
        // No loading spinner for this action, let it happen silently in the background
        return axios.delete(`${SERVER_URL}rest/cart/items`, {data: item})
            .then(() => dispatch(publishMessage({message: 'Item removed from your cart.', type: 'info'})))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    }
}

export function createOrder(items) {
    const action = {
        type: ActionType.CREATE_ORDER
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.post(`${SERVER_URL}rest/orders`, {items})
            .then(resp => {
                const id = extractIdFromLocation(resp);
                return Routing.transitionTo(Routes.order, {orderId: id});
            })
            .then(() => dispatch(asyncActionSuccess(action)))
            .then(() => dispatch(publishMessage({message: 'Order created.', type: 'success'})))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    }
}

export function loadOrder(orderId) {
    const action = {
        type: ActionType.LOAD_ORDER
    };
    return dispatch => {
        dispatch(asyncActionRequest(action));
        return axios.get(`${SERVER_URL}rest/orders/${orderId}`)
            .then(resp => dispatch(loadOrderSuccess(resp.data)))
            .catch(error => {
                if (error.response.data.message) {
                    dispatch(publishMessage({message: error.response.data.message, type: 'danger'}));
                }
                return dispatch(asyncActionFailure(action, error.response.data));
            });
    }
}
