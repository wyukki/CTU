import React from 'react';
import './App.css';
import {Router} from "react-router-dom";
import Routing from "./util/Routing";
import MainView from './component/MainView';
import appStore from "./store/Store";
import {Provider} from "react-redux";

/**
 * This is the second type of component in React - a functional component.
 *
 * When not considering state and lifecycle, React components are basically functions. They take properties (optional)
 * and return what should be rendered. Therefore, the simplest way of defining a component is to use a function which
 * returns stuff to render.
 *
 * The XML-like syntax is called JSX and it is transpiled into plain JavaScript. React contains predefined components
 * representing basic HTML elements like {@code div, a, p} etc. Additional components are defined by developers.
 */
const App = () => {
    return <Provider store={appStore}>
        <Router history={Routing._history}>
            <MainView/>
        </Router>
    </Provider>;
};

export default App;
