import {createHashHistory} from 'history';
import Routes from "./Routes";

export function setPathParams(path, params) {
    Object.getOwnPropertyNames(params).forEach(n => {
        path = path.replace(':' + n, params[n]);
    });
    return path;
}

const Routing = {
    _history: createHashHistory(),

    transitionTo(route, params = {}) {
        let path = route.path;
        path = setPathParams(path, params);
        this._history.push({pathname: path});
    },

    transitionToHome() {
        this.transitionTo(Routes.home);
    },

    getHistory() {
        return this._history;
    }
};

export default Routing;
