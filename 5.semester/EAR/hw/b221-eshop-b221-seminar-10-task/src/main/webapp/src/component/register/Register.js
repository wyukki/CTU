import React from 'react';
import PropTypes from 'prop-types';
import {Button, ButtonToolbar, Col, Input, Label, Modal, ModalBody, ModalHeader, Row} from "reactstrap";
import {connect} from "react-redux";
import './Register.css';
import {register} from "../../action/AsyncActions";

class Register extends React.Component {
    static propTypes = {
        open: PropTypes.bool,
        onClose: PropTypes.func.isRequired,
        register: PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            firstName: '',
            lastName: '',
            username: '',
            password: '',
            passwordConfirm: ''
        };
    }

    _onChange = (e) => {
        const upd = {};
        upd[e.target.name] = e.target.value;
        this.setState(upd);
    };

    _isValid() {
        return this.state.firstName.trim().length > 0 &&
            this.state.lastName.trim().length > 0 &&
            this.state.username.trim().length > 0 &&
            this.state.password.trim().length > 0 && this._passwordsMatch();
    }

    _passwordsMatch() {
        return this.state.password === this.state.passwordConfirm;
    }

    _register = () => {
        const profile = Object.assign({}, this.state);
        delete profile.passwordConfirm;
        this.props.register(profile).then(() => this.props.onClose());
    };

    _cancel = () => {
        this.props.onClose();
    };

    render() {
        return <Modal isOpen={this.props.open}>
            <ModalHeader>Login</ModalHeader>
            <ModalBody>
                <Row className='form-row'>
                    <Col md={6}>
                        <Label>First name</Label>
                        <Input type='text' name='firstName' bsSize='sm' value={this.state.firstName}
                               onChange={this._onChange}/>
                    </Col>
                    <Col md={6}>
                        <Label>Last name</Label>
                        <Input type='text' name='lastName' bsSize='sm' value={this.state.lastName}
                               onChange={this._onChange}/>
                    </Col>
                </Row>
                <Row className='form-row'>
                    <Col md={6}>
                        <Label>Username</Label>
                        <Input type='text' name='username' bsSize='sm' value={this.state.username}
                               onChange={this._onChange}/>
                    </Col>
                </Row>
                <Row className='form-row'>
                    <Col md={6}>
                        <Label>Password</Label>
                        <Input type='password' name='password' bsSize='sm' value={this.state.password}
                               onChange={this._onChange}/>
                    </Col>
                    <Col md={6}>
                        <Label>Password confirm</Label>
                        <Input type='password' name='passwordConfirm' bsSize='sm' value={this.state.passwordConfirm}
                               onChange={this._onChange}/>
                    </Col>
                </Row>
                <Row>
                    <Col md={12}>
                        <ButtonToolbar className='register-buttons'>
                            <Button color='success' size='sm' onClick={this._register}
                                    disabled={!this._isValid()}>Register</Button>
                            <Button color='link' size='sm' onClick={this._cancel}>Cancel</Button>
                        </ButtonToolbar>
                    </Col>
                </Row>
            </ModalBody>
        </Modal>;
    }
}

export default connect(null, dispatch => {
    return {
        register: profile => dispatch(register(profile))
    };
})(Register);