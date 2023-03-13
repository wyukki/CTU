import React from 'react';
import PropTypes from 'prop-types';
import {connect} from "react-redux";
import {loadCategory, loadCategoryProducts} from "../../action/AsyncActions";
import {Button, Col, Container, Row} from 'reactstrap';
import product from '../../images/product.png';
import {IfGranted} from 'react-authorization';
import Constants from "../../util/Constants";
import Routes from "../../util/Routes";
import Routing from "../../util/Routing";
import './CategoryProducts.css';

class CategoryProducts extends React.Component {

    static propTypes = {
        match: PropTypes.object,
        category: PropTypes.object,
        products: PropTypes.array,
        user: PropTypes.object,
        loadProducts: PropTypes.func,
        loadCategory: PropTypes.func
    };

    componentDidMount() {
        const catId = this.props.match.params.categoryId;
        if (!this.props.category) {
            this.props.loadCategory(catId);
        }
        this.props.loadProducts(catId);
    }

    componentDidUpdate(prevProps) {
        const catId = this.props.match.params.categoryId;
        const prevCatId = prevProps.match.params.categoryId;
        if (catId !== prevCatId) {
            this.props.loadProducts(catId);
        }
    }

    _createProduct = () => {
        Routing.transitionTo(Routes.createProduct);
    };

    _openProduct = (product) => {
        Routing.transitionTo(Routes.product, {productId: product.id});
    };

    render() {
        const catName = this.props.category ? this.props.category.name : '',
            userRole = this.props.user ? this.props.user.role : '';
        return <Container fluid={true}>
            <Row>
                <Col md={6}>
                    <h3>{catName + ' - Products'}</h3>
                </Col>
                <IfGranted expected={Constants.ROLE_ADMIN} actual={userRole}>
                    <Col md={6} className='pull-right'>
                        <Button color='primary' size='sm' onClick={this._createProduct}>Create Product</Button>
                    </Col>
                </IfGranted>
            </Row>
            {this._renderProducts()}
        </Container>;
    }

    _renderProducts() {
        const products = this.props.products;
        if (products.length === 0) {
            return <div className='italic'>There are currently no products in this category.</div>
        }
        const toRender = [];
        let row = [];
        for (let i = 0, len = products.length; i < len; i++) {
            row.push(<Col md={4} key={products[i].id}>
                <div className='center product-preview'>
                    <Button color='link' onClick={() => this._openProduct(products[i])}>
                        <img src={product} alt='Product visualization'/>
                        <div>{products[i].name}</div>
                        <div>{products[i].price + ' CZK'}</div>
                    </Button>
                </div>
            </Col>);
            if (i > 0 && i % 3 === 0) {
                toRender.push(<Row key={i}>{row}</Row>);
                row = [];
            }
        }
        if (row.length > 0) {
            toRender.push(<Row key={products.length + 1}>{row}</Row>);
        }
        return toRender;
    }
}

export default connect(state => {
    return {
        category: state.selectedCategory,
        products: state.products,
        user: state.user
    };
}, dispatch => {
    return {
        loadProducts: categoryId => dispatch(loadCategoryProducts(categoryId)),
        loadCategory: categoryId => dispatch(loadCategory(categoryId))
    };
})(CategoryProducts);
