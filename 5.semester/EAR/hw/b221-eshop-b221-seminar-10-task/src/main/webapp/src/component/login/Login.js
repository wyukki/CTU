import React from 'react';
import PropTypes from 'prop-types';
import {Button, ButtonToolbar, Col, Input, Label, Modal, ModalBody, ModalHeader, Row} from "reactstrap";
import {connect} from "react-redux";
import {login} from "../../action/AsyncActions";
import './Login.css';

class Login extends React.Component {

    static propTypes = {
        open: PropTypes.bool,
        onClose: PropTypes.func,
        login: PropTypes.func
    };

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: ''
        };
    }

    _onChange = (e) => {
        const upd = {};
        upd[e.target.name] = e.target.value;
        this.setState(upd);
    };

    _login = () => {
        this.props.login(this.state.username, this.state.password).then(() => this.props.onClose());
    };

    _cancel = () => {
        this.props.onClose();
    };

    render() {
        return <Modal isOpen={this.props.open}>
            <ModalHeader>Login</ModalHeader>
            <ModalBody>
                <Row className='form-row'>
                    <Col>
                        <Label>Username</Label>
                        <Input type='text' name='username' bsSize='sm' value={this.state.username}
                               onChange={this._onChange}/>
                    </Col>
                </Row>
                <Row className='form-row'>
                    <Col>
                        <Label>Password</Label>
                        <Input type='password' name='password' bsSize='sm' value={this.state.password}
                               onChange={this._onChange}/>
                    </Col>
                </Row>
                <Row>
                    <Col md={8}>&nbsp;</Col>
                    <Col md={4}>
                        <ButtonToolbar className='login-buttons'>
                            <Button color='success' size='sm' onClick={this._login}>Login</Button>
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
        login: (username, password) => dispatch(login(username, password))
    }
})(Login);
