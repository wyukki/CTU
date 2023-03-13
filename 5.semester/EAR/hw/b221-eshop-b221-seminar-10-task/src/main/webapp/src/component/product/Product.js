import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import {withRouter} from "react-router";
import {connect} from "react-redux";
import {loadProduct} from "../../action/AsyncActions";
import {Container, Nav, NavItem, NavLink, TabContent, TabPane} from "reactstrap";
import ProductDetail from "./ProductDetail";
import Constants from "../../util/Constants";
import EditProduct from "./EditProduct";
import './Product.css';

class Product extends React.Component {
    static propTypes = {
        match: PropTypes.object.isRequired,
        product: PropTypes.object,
        loadProduct: PropTypes.func.isRequired,
        user: PropTypes.object
    };

    constructor(props) {
        super(props);
        this.state = {
            activeTab: 'detail'
        };
    }

    componentDidMount() {
        const productId = this.props.match.params.productId;
        this.props.loadProduct(productId);
    }

    _toggleTab = (tabId) => {
        this.setState({activeTab: tabId});
    };

    render() {
        const product = this.props.product;
        if (!product) {
            return <Container fluid={true}><h3>Product detail</h3></Container>;
        }
        let component;
        if (this.props.user && this.props.user.role === Constants.ROLE_ADMIN) {
            component = this._renderProductTabs();
        } else {
            component = <ProductDetail product={product}/>;
        }
        return <Container fluid={true}>
            <h3>{product.name}</h3>
            {component}
        </Container>;
    }

    _renderProductTabs() {
        return <div>
            <Nav tabs={true}>
                <NavItem>
                    <NavLink className={classNames({active: this.state.activeTab === 'detail'})}
                             onClick={() => this._toggleTab('detail')}>Detail</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink className={classNames({active: this.state.activeTab === 'edit'})}
                             onClick={() => this._toggleTab('edit')}>Edit</NavLink>
                </NavItem>
            </Nav>
            <TabContent activeTab={this.state.activeTab}>
                <TabPane tabId='detail'>
                    <ProductDetail product={this.props.product}/>
                </TabPane>
                <TabPane tabId='edit'>
                    <EditProduct product={this.props.product}/>
                </TabPane>
            </TabContent>
        </div>;
    }
}

export default connect(state => {
    return {
        user: state.user,
        product: state.product
    };
}, dispatch => {
    return {
        loadProduct: productId => dispatch(loadProduct(productId))
    };
})(withRouter(Product));