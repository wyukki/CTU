import React from 'react';
import PropTypes from 'prop-types';
import {Badge, Button, ButtonToolbar, Col, Container, Input, Label, Row} from 'reactstrap';
import {connect} from "react-redux";
import {createProduct, removeProduct, updateProduct} from "../../action/AsyncActions";
import {selectCategory} from "../../action/SyncActions";
import {Typeahead} from 'react-bootstrap-typeahead';
import 'react-bootstrap-typeahead/css/Typeahead.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTimes} from '@fortawesome/free-solid-svg-icons';

class EditProduct extends React.Component {
    static propTypes = {
        product: PropTypes.object,
        categories: PropTypes.array,
        createProduct: PropTypes.func.isRequired,
        updateProduct: PropTypes.func.isRequired,
        removeProduct: PropTypes.func.isRequired,
        selectCategory: PropTypes.func.isRequired
    };

    static defaultProps = {
        product: {
            name: '',
            amount: 1,
            price: 1.0,
            categories: []
        }
    };

    constructor(props) {
        super(props);
        const product = props.product;
        this.state = {
            name: product.name,
            amount: product.amount,
            price: product.price,
            categories: product.categories
        };
        this.typeahead = React.createRef();
    }

    componentDidUpdate(prevProps) {
        if (this.props.product !== prevProps.product) {
            this.setState({...this.props.product});
        }
    }

    _onSave = () => {
        const product = Object.assign({}, this.state);
        if (this._isNew()) {
            this.props.createProduct(product);
        } else {
            product.id = this.props.product.id;
            this.props.updateProduct(product);
        }
    };

    _onRemove = () => {
        this.props.removeProduct(this.props.product);
    };

    _isNew() {
        return this.props.product.id === undefined;
    }

    _onNameChange = (e) => {
        this.setState({name: e.target.value});
    };

    _onNumChange = (e) => {
        const change = {};
        change[e.target.name] = Number(e.target.value);
        this.setState(change);
    };

    _onAddCategory = (selected) => {
        this.setState({categories: this.state.categories.concat(selected)});
        if (selected.length) {
            this.typeahead.current.clear();
        }
    };

    _onRemoveCategory = (category) => {
        const newCategories = this.state.categories.slice();
        newCategories.splice(newCategories.indexOf(category), 1);
        this.setState({categories: newCategories});
    };

    render() {
        return <Container fluid={true} className='tab-content'>
            <h3>{this._isNew() ? 'New product' : this.props.product.name + ' - Update'}</h3>
            <Row className='form-row'>
                <Col md={6}>
                    <Label>Name</Label>
                    <Input type='text' bsSize='small' value={this.state.name} onChange={this._onNameChange}/>
                </Col>
            </Row>
            <Row className='form-row'>
                <Col md={6}>
                    <Label>Amount</Label>
                    <Input type='number' name='amount' min={0} bsSize='small' value={this.state.amount}
                           onChange={this._onNumChange}/>
                </Col>
            </Row>
            <Row className='form-row'>
                <Col md={6}>
                    <Label>Price</Label>
                    <Input type='number' name='price' min={0} bsSize='small' value={this.state.price} step={0.01}
                           onChange={this._onNumChange}/>
                </Col>
            </Row>
            <Row className='form-row'>
                <Col md={6}>
                    <Label>Categories</Label>
                    <div className='categories'>
                        {this._renderCategories()}
                    </div>
                    <Typeahead id="product-category-selector" ref={this.typeahead} bsSize='sm'
                               onChange={this._onAddCategory} options={this._getCategoriesToAdd()} labelKey='name'/>
                </Col>
            </Row>
            <Row>
                <Col md={6}>
                    <ButtonToolbar className='pull-right'>
                        <Button size='sm' color='success' onClick={this._onSave}>Save</Button>
                        {!this._isNew() && <Button size='sm' color='danger' onClick={this._onRemove}>Remove</Button>}
                    </ButtonToolbar>
                </Col>
            </Row>
        </Container>;
    }

    _renderCategories() {
        const categories = this.state.categories ? this.state.categories : [];
        return categories.map(cat => <Badge key={cat.id} color='info'><span
            onClick={() => this.props.selectCategory(cat)}>{cat.name}</span><FontAwesomeIcon
            icon={faTimes} className='remove-category-icon' onClick={() => this._onRemoveCategory(cat)}/></Badge>);
    }

    _getCategoriesToAdd() {
        const categories = this.props.categories ? this.props.categories : [];
        const existing = this.state.categories ? this.state.categories : [];
        return categories.filter(cat => existing.find(cc => cc.id === cat.id) === undefined);
    }
}

export default connect(state => {
    return {
        categories: state.categories
    }
}, dispatch => {
    return {
        createProduct: product => dispatch(createProduct(product)),
        updateProduct: product => dispatch(updateProduct(product)),
        removeProduct: product => dispatch(removeProduct(product)),
        selectCategory: catId => dispatch(selectCategory(catId))
    };
})(EditProduct);
