# EAR E-shop Frontend

Written in [React](https://reactjs.org/), using:
* [Bootstrap](https://getbootstrap.com/) - more specifically, the [Reactstrap](https://reactstrap.github.io/) component library
* [Redux](https://redux.js.org/) - maintaining application state
* [React router](https://reactrouter.com/) - routing
* [Axios](https://github.com/axios/axios) - for HTTP requests to the backend REST API

Detailed tutorial is available on the React [website](https://reactjs.org/docs/getting-started.html), this is just a TL;DR to get the basic ideas.

### Components

React components are plain JavaScript (JS) classes/functions which return what the React framework should render. To simplify
matters, an XML-like syntax called [JSX](https://reactjs.org/docs/introducing-jsx.html) is used. 
This syntax is transpiled into plain JS calls during project build. Two basic types of components exist:
* Classes with `render` method extending the `React.Component` class. `render` returns the things to render. See `MainView.js` for more detailed explanation.
* Functions returning the things to render. See `App.js` for more detailed explanation.

Simply put, React is based on the concept of a virtual DOM. This represents the actual rendered content of the page. When an update
is made, React compares the new virtual DOM with the existing, finds elements which need to be re-rendered and changes only them
in the actual browser DOM. This considerably speeds rendering.
This strategy also makes application programming quite simple - the application is basically re-rendered within React on every change.
React then ensures that only the smallest set of changes is applied to the real DOM.

Another important concept in React is the distinction between `props` and `state`.

#### Props

React's philosophy is that data flow for more general components to the more detailed ones. `props` represent the mechanism of this one directional
data flow. Component props are basically parameters, which influence what the component will render.

#### State

Most components in React are (and should be) stateful functions of their props. However, this does not (and cannot) hold universally. Some components
have to take care of the state of the application. These components have their `state`, which can be passed down to their children as `props`.

Note that state changes cannot be done directly to the state object. Instead, an asynchronous method called `setState` is called with the change
to be applied to the state. After its invocation, the component (and its sub-tree) will be re-rendered.
Both state and props are immutable, so defensive copying should be used.

### Application State

Having state only in components is not a viable strategy for larger applications. These need to maintain some form of global state. React came up
with a pattern called __flux__ which was supposed to overcome some of the issues of regular MVC w.r.t. the relationship between components and the event handlers
which work with the model.

Flux knows three concepts:
1. Store - keeps the state of the application and performs its transitions
2. Actions - invoked by components or other actors. The store receives action objects and reacts to them appropriately
3. Components - regular React components which listen to store updates and render state data

One of the most popular libraries which implements Flux (with some modifications) is __Redux__. In Redux, the store is just a holder for the state.
All changes to the state are made by `reducers` -- pure functions which define the business logic of transformations of the state based on specific
actions.

In this e-shop (and in most common React Redux-based applications), you can find the actions in the `actions` folder, reducers in the `reducer` directory,
and the store (which is really simple) in the `store` directory.

### Routing

Although e-shop is a single page application (there are no refreshes of the browser window by default), it is beneficial to perform routing - updates of the
browser URL. This indicates to the user that a transition was made and allows them to bookmark specific parts of the application.

For this purpose, the __React router__ library is used. It allows to programmatically control the transitions (when necessary), contains `Link` components
which work as links within the application and, most importantly, renders different views based on the current URL. Basically, on different levels of the
application, a `Switch` can be used to decide which component will be rendered based on the current path.


## Build

The application is built using [Webpack](https://webpack.js.org/). This is a tool which takes application source code, its dependencies (defined in `package.json`)
and creates a single bundle (can be split into multiple chunks to speed up loading) containing the application. This bundle is usually pretty big (tens of thousands
of lines of code) so __minification__ is used to reduce its data size.

Another common tool used for building React applications is [Babel](https://babeljs.io/) which is able to transpile JS code into a form digestible even
for older browsers. In addition, it handles the transformation of JSX into plain JS.

To run these tools, we use [Node.js](https://nodejs.org/en/) - a JavaScript application server.
