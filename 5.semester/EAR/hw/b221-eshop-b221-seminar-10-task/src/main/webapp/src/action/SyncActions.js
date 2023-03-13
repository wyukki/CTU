import AsyncStatus from "./AsyncStatus";
import ActionType from "./ActionType";
import Routing from "../util/Routing";
import Routes from "../util/Routes";

/**
 * Sync functions are just functions that return action objects which are then passed to the Redux store.
 */

export function asyncActionRequest(action) {
    return Object.assign(action, {
        asyncStatus: AsyncStatus.REQUEST
    });
}

export function asyncActionSuccess(action) {
    return Object.assign(action, {
        asyncStatus: AsyncStatus.SUCCESS
    });
}

export function asyncActionFailure(action, error) {
    return Object.assign(action, {
        asyncStatus: AsyncStatus.ERROR,
        error
    });
}

export function loadUserSuccess(data) {
    return asyncActionSuccess({
        type: ActionType.LOAD_USER,
        user: data
    });
}

export function loadCategoriesSuccess(data) {
    return asyncActionSuccess({
        type: ActionType.LOAD_CATEGORIES,
        categories: data
    });
}

export function loadCategoryProductsSuccess(data) {
    return asyncActionSuccess({
        type: ActionType.LOAD_CATEGORY_PRODUCTS,
        products: data
    });
}

export function logoutSuccess() {
    return asyncActionSuccess({
        type: ActionType.LOGOUT
    });
}

export function createCategorySuccess() {
    return asyncActionSuccess({
        type: ActionType.CREATE_CATEGORY
    });
}

export function selectCategory(category) {
    Routing.transitionTo(Routes.category, {categoryId: category.id});
    return {
        type: ActionType.SELECT_CATEGORY,
        category
    };
}

export function loadCategorySuccess(category) {
    return asyncActionSuccess({
        type: ActionType.LOAD_CATEGORY,
        category
    });
}

export function loadProductSuccess(product) {
    return asyncActionSuccess({
        type: ActionType.LOAD_PRODUCT,
        product
    });
}

export function addToLocalCart(item) {
    return {
        type: ActionType.ADD_TO_CART,
        item
    };
}

export function removeFromLocalCart(item) {
    return {
        type: ActionType.REMOVE_FROM_CART,
        item
    };
}

export function loadOrderSuccess(order) {
    return asyncActionSuccess({
        type: ActionType.LOAD_ORDER,
        order
    });
}

export function publishMessage(message) {
    return {
        type: ActionType.PUBLISH_MESSAGE,
        message
    }
}

export function dismissMessage(message) {
    return {
        type: ActionType.DISMISS_MESSAGE,
        message
    };
}
