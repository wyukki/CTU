import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Container, Row, Table} from "reactstrap";
import {loadOrder} from "../../action/AsyncActions";
import {connect} from "react-redux";
import Routing from "../../util/Routing";
import Routes from "../../util/Routes";

class Order extends React.Component {
    static propTypes = {
        match: PropTypes.object.isRequired,
        order: PropTypes.object,
        loadOrder: PropTypes.func.isRequired
    };

    componentDidMount() {
        const orderId = this.props.match.params.orderId;
        this.props.loadOrder(orderId);
    }

    _openProduct = (product) => {
        Routing.transitionTo(Routes.product, {productId: product.id});
    };

    render() {
        const order = this.props.order;
        if (!order) {
            return null;
        }
        return <Container fluid={true}>
            <h3>{'Order No. ' + order.id}</h3>
            <Row>
                <Col md={4}>
                    {'Created: ' + new Date(order.created).toLocaleString()}
                </Col>
            </Row>
            <Row>
                <Col md={12}>
                    <Table striped={true} className='cart-items'>
                        <thead>
                        <tr>
                            <th>Product</th>
                            <th>Amount</th>
                            <th>Price</th>
                        </tr>
                        </thead>
                        <tbody>
                        {this._renderItems()}
                        </tbody>
                    </Table>
                </Col>
            </Row>
        </Container>;
    }

    _renderItems() {
        const items = this.props.order.items;
        return items.map(item => <tr key={item.product.id}>
            <td><Button color='link' onClick={() => this._openProduct(item.product)}>{item.product.name}</Button></td>
            <td>{item.amount}</td>
            <td>{item.amount * item.product.price + ' CZK'}</td>
        </tr>);
    }
}

export default connect(state => {
    return {
        order: state.order
    };
}, dispatch => {
    return {
        loadOrder: orderId => dispatch(loadOrder(orderId))
    };
})(Order);
