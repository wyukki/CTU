import React from 'react';
import PropTypes from 'prop-types';
import {Badge, Button, Col, Container, Input, Label, Row} from "reactstrap";
import productImg from "../../images/product.png";
import {selectCategory} from "../../action/SyncActions";
import {connect} from "react-redux";
import {addToCart} from "../../action/AsyncActions";

class ProductDetail extends React.Component {
    static propTypes = {
        product: PropTypes.object.isRequired,
        user: PropTypes.object,
        selectCategory: PropTypes.func.isRequired,
        addToCart: PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            itemCount: 1
        };
    }

    _updateItemCount = (e) => {
        let cnt = Number(e.target.value);
        if (cnt < 1 || cnt > this.props.product.amount) {
            cnt = 1;
        }
        this.setState({itemCount: cnt});
    };

    _addToCart = () => {
        const item = {
            product: this.props.product,
            amount: this.state.itemCount
        };
        this.props.addToCart(item, this.props.user);
    };

    render() {
        const product = this.props.product;
        return <Container fluid={true} className='tab-content'>
            <Row>
                <Col md={6}>
                    <div className='center'>
                        <img src={productImg} alt='Product visualization'/>
                    </div>
                </Col>
                <Col md={6}>
                    <Row>
                        <Col md={12}>
                            <h4><Label>{product.price + ' CZK'}</Label></h4>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={12}>
                            {this._renderAmount()}
                        </Col>
                    </Row>
                    <Row>
                        <Col md={12}>
                            <Row>
                                <Col md={2}>
                                    <Input type='number' bsSize='sm' value={this.state.itemCount} size='3' min={1}
                                           max={this.props.product.amount} onChange={this._updateItemCount}/>
                                </Col>
                                <Col md={3}>
                                    <Button color='primary' size='sm' onClick={this._addToCart}>Add to cart</Button>
                                </Col>
                            </Row>
                        </Col>
                    </Row>
                </Col>
            </Row>
            <Row>
                <Col md={1}>
                    <h5>Categories</h5>
                </Col>
                <Col md={11} className='categories'>
                    {this._renderCategories()}
                </Col>
            </Row>
        </Container>;
    }

    _renderAmount() {
        const amount = this.props.product.amount;
        if (amount > 0) {
            return <Label>{'In stock: ' + amount + ' items'}</Label>
        } else {
            return <Label>Sold out</Label>
        }
    }

    _renderCategories() {
        const categories = this.props.product.categories ? this.props.product.categories : [];
        return categories.map(cat => <Badge key={cat.id} color='info'
                                            onClick={() => this.props.selectCategory(cat)}>{cat.name}</Badge>);
    }
}

export default connect(state => {
    return {
        user: state.user
    };
}, dispatch => {
    return {
        selectCategory: catId => dispatch(selectCategory(catId)),
        addToCart: (item, user) => dispatch(addToCart(item, user))
    };
})(ProductDetail);