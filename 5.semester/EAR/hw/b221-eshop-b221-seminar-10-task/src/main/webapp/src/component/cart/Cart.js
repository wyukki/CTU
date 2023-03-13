import React from 'react';
import PropTypes from 'prop-types';
import {connect} from "react-redux";
import {addToCart, createOrder, removeFromCart} from "../../action/AsyncActions";
import {Button, Col, Container, Input, Label, Row, Table} from "reactstrap";
import Routing from "../../util/Routing";
import Routes from "../../util/Routes";
import './Cart.css';

class Cart extends React.Component {
    static propTypes = {
        cart: PropTypes.array.isRequired,
        user: PropTypes.object,
        removeItem: PropTypes.func.isRequired,
        addItem: PropTypes.func.isRequired,
        createOrder: PropTypes.func.isRequired
    };

    _openProduct = (product) => {
        Routing.transitionTo(Routes.product, {productId: product.id});
    };

    _calculateCartPrice() {
        return this.props.cart.reduce((acc, val) => acc + val.amount * val.product.price, 0);
    }

    _updateItemAmount = (item, e) => {
        if (e.target.value.trim().length === 0) {
            return;
        }
        const newAmount = Number(e.target.value);
        if (newAmount < item.amount) {
            this.props.removeItem({product: item.product, amount: item.amount - newAmount}, this.props.user);
        } else {
            this.props.addItem({product: item.product, amount: newAmount - item.amount}, this.props.user);
        }
    };

    _createOrder = () => {
        this.props.createOrder(this.props.cart);
    };

    render() {
        if (this.props.cart.length === 0) {
            return <Container fluid={true}>
                <h3>Cart</h3>
                <div className='italic'>Your cart is currently empty.</div>
            </Container>;
        }
        return <Container fluid={true}>
            <h3>Cart</h3>
            <Row>
                <Col md={12}>
                    <Table striped={true} className='cart-items'>
                        <thead>
                        <tr>
                            <th>Product</th>
                            <th>Amount</th>
                            <th>Price</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {this._renderItems()}
                        </tbody>
                    </Table>
                </Col>
            </Row>
            <Row>
                <Col md={12} className='pull-right'>
                    <hr/>
                    <h4><Label>{'Total: ' + this._calculateCartPrice() + ' CZK'}</Label></h4>
                </Col>
            </Row>
            <Row>
                <Col md={12} className='pull-right'>
                    <Button color='primary' size='sm' onClick={this._createOrder}>Create order</Button>
                </Col>
            </Row>
        </Container>;
    }

    _renderItems() {
        const items = this.props.cart;
        return items.map(item => <tr key={item.product.id}>
            <td><Button color='link' onClick={() => this._openProduct(item.product)}>{item.product.name}</Button></td>
            <td>
                <Input type='number' bsSize='sm' value={item.amount} min={0} max={item.product.amount}
                       onChange={e => this._updateItemAmount(item, e)}/>
            </td>
            <td>{item.amount * item.product.price + ' CZK'}</td>
            <td><Button size='sm' color='danger' onClick={() => this.props.removeItem(item)}>Remove</Button></td>
        </tr>);
    }
}

export default connect(state => {
    return {
        cart: state.cart,
        user: state.user
    };
}, dispatch => {
    return {
        addItem: (item, user) => dispatch(addToCart(item, user)),
        removeItem: (item, user) => dispatch(removeFromCart(item, user)),
        createOrder: (items) => dispatch(createOrder(items))
    };
})(Cart);

