import React from 'react';
import PropTypes from 'prop-types';
import {
    Col,
    DropdownItem,
    DropdownMenu,
    DropdownToggle,
    Nav,
    Navbar,
    NavbarBrand,
    Row,
    UncontrolledDropdown
} from 'reactstrap';
import CategoryList from "./category/CategoryList";
import Content from "./Content";
import {connect} from "react-redux";
import Mask from "./misc/Mask";
import {Route, Switch, withRouter} from "react-router-dom";
import CategoryProducts from "./product/CategoryProducts";
import Routes from "../util/Routes";
import Login from "./login/Login";
import {loadUser, logout} from "../action/AsyncActions";
import CreateCategory from "./category/CreateCategory";
import Messages from "./message/Messages";
import Product from "./product/Product";
import EditProduct from "./product/EditProduct";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faShoppingCart} from "@fortawesome/free-solid-svg-icons";
import './MainView.css';
import Cart from "./cart/Cart";
import Routing from "../util/Routing";
import Register from "./register/Register";
import Order from "./order/Order";

/**
 * This is a more traditional type of a React component - a component class.
 *
 * It can have lifecycle methods which are invoked by React itself during rendering.
 *
 * The most important method is the {@code render} method, whose return value is what React will render to the user.
 */
class MainView extends React.Component {

    static propTypes = {
        loading: PropTypes.bool,
        user: PropTypes.object,
        cart: PropTypes.array,
        logout: PropTypes.func.isRequired,
        loadUser: PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            loginOpen: false,
            registerOpen: false
        };
    }

    componentDidMount() {
        if (!this.props.user) {
            this.props.loadUser();
        }
    }

    _openLogin = () => {
        this.setState({loginOpen: true});
    };

    _closeLogin = () => {
        this.setState({loginOpen: false});
    };

    _openRegister = () => {
        this.setState({registerOpen: true});
    };

    _closeRegister = () => {
        this.setState({registerOpen: false});
    };

    _logout = () => {
        this.props.logout();
    };

    _openCart = () => {
        Routing.transitionTo(Routes.cart);
    };

    render() {
        return <div>
            {this._renderDropdown()}
            <Messages/>
            <div className='container-fluid app-container'>
                {this.props.loading && <Mask/>}
                <Login open={this.state.loginOpen} onClose={this._closeLogin}/>
                <Register open={this.state.registerOpen} onClose={this._closeRegister}/>
                <h1>EAR E-shop</h1>
                <hr/>
                <Row>
                    <Col md={2}>
                        <CategoryList/>
                    </Col>
                    <Col md={10}>
                        <Switch>
                            <Route path={Routes.createCategory.path} component={CreateCategory}/>
                            <Route path={Routes.category.path} component={CategoryProducts}/>
                            <Route path={Routes.createProduct.path} component={EditProduct}/>
                            <Route path={Routes.product.path} component={Product}/>
                            <Route path={Routes.cart.path} component={Cart}/>
                            <Route path={Routes.order.path} component={Order}/>
                            <Route exact={true} component={Content}/>
                        </Switch>
                    </Col>
                </Row>
            </div>
        </div>
    }

    _renderDropdown() {
        const user = this.props.user;
        return <Navbar color="light" light={true} expand={"md"} className={"d-flex"}>
            <NavbarBrand href='#'>EAR</NavbarBrand>
            <Nav navbar={true} className="flex-grow-1"/>
            <Nav navbar={true}>
                <span className='cart' title='Cart' onClick={this._openCart}>
                    <FontAwesomeIcon icon={faShoppingCart}/>
                    {this.props.cart.length > 0 && <span className='cart-content'>
                    {this.props.cart.length}
                    </span>
                    }
                </span>
            </Nav>
            <Nav navbar={true}>
                <UncontrolledDropdown nav={true} inNavbar={true}>
                    <DropdownToggle nav={true} caret={true}>
                        {user ? user.firstName + ' ' + user.lastName : 'Options'}
                    </DropdownToggle>
                    <DropdownMenu right={true}>
                        {this._renderRegisterItem()}
                        {user ? this._renderLogoutItem() : this._renderLoginItem()}
                    </DropdownMenu>
                </UncontrolledDropdown>
            </Nav>
        </Navbar>;
    }

    _renderLogoutItem() {
        return <DropdownItem onClick={this._logout}>Logout</DropdownItem>;
    }

    _renderLoginItem() {
        return <DropdownItem onClick={this._openLogin}>Login</DropdownItem>;
    }

    _renderRegisterItem() {
        return this.props.user ? null : <DropdownItem onClick={this._openRegister}>Register</DropdownItem>;
    }
}

export default connect(state => {
    return {
        loading: state.loading,
        user: state.user,
        cart: state.cart
    };
}, dispatch => {
    return {
        logout: () => dispatch(logout()),
        loadUser: () => dispatch(loadUser())
    }
})(withRouter(MainView));
