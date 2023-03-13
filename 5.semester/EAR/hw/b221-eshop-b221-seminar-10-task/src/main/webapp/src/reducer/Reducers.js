import ActionType from '../action/ActionType';
import {combineReducers} from "redux";
import AsyncStatus from "../action/AsyncStatus";

/**
 * Reducers transform the application state based on action objects.
 *
 * Notice how each part of the state has its own reducer. This split helps to keep the code clean and readable.
 */

function user(state = null, action) {
    switch (action.type) {
        case ActionType.LOAD_USER:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return action.user;
            } else {
                return state;
            }
        case ActionType.LOGOUT:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return null;
            } else {
                return state;
            }
        default:
            return state;
    }
}

function categories(state = [], action) {
    switch (action.type) {
        case ActionType.LOAD_CATEGORIES:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return action.categories;
            } else {
                return state;
            }
        case ActionType.LOGOUT:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return [];
            } else {
                return state;
            }
        default:
            return state;
    }
}

function selectedCategory(state = null, action) {
    switch (action.type) {
        case ActionType.SELECT_CATEGORY:
            return action.category;
        case ActionType.LOAD_CATEGORY:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return action.category;
            } else {
                return state;
            }
        case ActionType.LOGOUT:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return null;
            } else {
                return state;
            }
        default:
            return state;
    }
}

function products(state = [], action) {
    switch (action.type) {
        case ActionType.LOAD_CATEGORY_PRODUCTS:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return action.products;
            } else {
                return state;
            }
        case ActionType.LOGOUT:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return [];
            } else {
                return state;
            }
        default:
            return state;
    }
}

function loading(state = false, action) {
    if (action.asyncStatus) {
        switch (action.asyncStatus) {
            case AsyncStatus.REQUEST:
                return true;
            default:
                return false;
        }
    }
    return state;
}

function messages(state = [], action) {
    switch (action.type) {
        case ActionType.PUBLISH_MESSAGE:
            return [...state, action.message];
        case ActionType.DISMISS_MESSAGE:
            const newArr = state.slice(0);
            newArr.splice(newArr.indexOf(action.message), 1);
            return newArr;
        default:
            return state;
    }
}

function product(state = null, action) {
    switch (action.type) {
        case ActionType.LOAD_PRODUCT:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return action.product;
            } else {
                return state;
            }
        case ActionType.LOGOUT:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return null;
            } else {
                return state;
            }
        default:
            return state;
    }
}

function cart(state = [], action) {
    switch (action.type) {
        case ActionType.ADD_TO_CART:
            const presentItem = state.find(it => it.product.id === action.item.product.id);
            if (presentItem) {
                const newItem = Object.assign({}, presentItem, {amount: presentItem.amount + action.item.amount});
                const newState = state.slice();
                newState.splice(newState.indexOf(presentItem), 1, newItem);
                return newState;
            } else {
                const newState = state.slice();
                newState.push(action.item);
                return newState;
            }
        case ActionType.REMOVE_FROM_CART:
            const existing = state.find(it => it.product.id === action.item.product.id);
            if (existing.amount === action.item.amount) {
                const newState = state.slice();
                newState.splice(newState.indexOf(existing), 1);
                return newState;
            } else {
                const newItem = Object.assign({}, existing, {amount: existing.amount - action.item.amount});
                const newState = state.slice();
                newState.splice(newState.indexOf(existing), 1, newItem);
                return newState;
            }
        case ActionType.LOAD_USER:
            if (action.asyncStatus === AsyncStatus.SUCCESS && action.user.cart) {
                return action.user.cart.items;
            } else {
                return state;
            }
        case ActionType.CREATE_ORDER:// Intentional fall-through
        case ActionType.LOGOUT:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return [];
            } else {
                return state;
            }
        default:
            return state;
    }
}

function order(state = null, action) {
    switch (action.type) {
        case ActionType.LOAD_ORDER:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return action.order;
            } else {
                return state;
            }
        case ActionType.LOGOUT:
            if (action.asyncStatus === AsyncStatus.SUCCESS) {
                return null;
            } else {
                return state;
            }
        default:
            return state;
    }
}

/**
 * Here we combine the individual reducers into one state object. Reducer (function) names are used as names of the state
 * attributes. Therefore, our application state has an attribute called loading, cart, categories, etc.
 */

const rootReducer = combineReducers({
    loading,
    cart,
    categories,
    messages,
    order,
    product,
    products,
    selectedCategory,
    user
});

export default rootReducer;
