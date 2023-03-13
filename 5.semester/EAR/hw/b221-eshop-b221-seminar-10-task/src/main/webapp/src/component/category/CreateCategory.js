import React from 'react';
import PropTypes from 'prop-types';
import {Button, ButtonToolbar, Card, CardBody, CardHeader, Col, Input, Label, Row} from "reactstrap";
import {connect} from "react-redux";
import {createCategory} from "../../action/AsyncActions";
import Routing from "../../util/Routing";

class CreateCategory extends React.Component {

    static propTypes = {
        createCategory: PropTypes.func
    };

    constructor(props) {
        super(props);
        this.state = {
            name: ''
        };
    }

    _onChange = (e) => {
        this.setState({name: e.target.value});
    };

    _onCreate = () => {
        this.props.createCategory(this.state.name);
    };

    _onCancel = () => {
        Routing.transitionToHome();
    };

    render() {
        return <Card>
            <CardHeader>
                <h5>Create category</h5>
            </CardHeader>
            <CardBody>
                <Row className='form-row'>
                    <Col md={6}>
                        <Label>Name</Label>
                        <Input type='text' value={this.state.name} onChange={this._onChange}/>
                    </Col>
                </Row>
                <Row>
                    <Col md={6}>
                        <ButtonToolbar>
                            <Button color='success' size='sm' onClick={this._onCreate}>Create</Button>
                            <Button size='sm' onClick={this._onCancel}>Cancel</Button>
                        </ButtonToolbar>
                    </Col>
                </Row>
            </CardBody>
        </Card>;
    }
}

export default connect(null, dispatch => {
    return {
        createCategory: name => dispatch(createCategory(name))
    };
})(CreateCategory);