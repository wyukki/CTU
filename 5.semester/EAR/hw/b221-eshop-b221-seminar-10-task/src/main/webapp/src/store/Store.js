import {applyMiddleware, createStore} from 'redux';
import {createLogger} from 'redux-logger';
import thunk from 'redux-thunk';
import reducer from '../reducer/Reducers';

const loggerMiddleware = createLogger();

/**
 * The store uses our reducers and some middleware libraries.
 */

const store = createStore(reducer, applyMiddleware(thunk, loggerMiddleware));

export default store;
