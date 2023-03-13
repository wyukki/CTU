import React from 'react';
import PropTypes from 'prop-types';
import {Button, ButtonToolbar, Card, CardBody, CardHeader, Container, Jumbotron} from "reactstrap";
import {IfGranted} from 'react-authorization';
import Routing from "../util/Routing";
import Routes from "../util/Routes";
import {connect} from "react-redux";
import Constants from "../util/Constants";

const Content = (props) => {
    const userRole = props.user ? props.user.role : '';
    return <div><Jumbotron fluid={true}>
        <Container fluid={true}>
            <h2>Welcome to EAR e-shop</h2>
            <hr/>
            <p className='lead'>
                Pick a category to view products in it.
            </p>
        </Container>
    </Jumbotron>
        <IfGranted expected={Constants.ROLE_ADMIN} actual={userRole}>
            <Card>
                <CardHeader>
                    <h5>Admin</h5>
                </CardHeader>
                <CardBody>
                    <ButtonToolbar>
                        <Button color='primary' onClick={() => Routing.transitionTo(Routes.createCategory)}>Create
                            category</Button>
                        <Button color='primary' onClick={() => Routing.transitionTo(Routes.createProduct)}>Create
                            product</Button>
                    </ButtonToolbar>
                </CardBody>
            </Card>
        </IfGranted>
    </div>;
};

Content.propTypes = {
    user: PropTypes.object
};

export default connect(state => {
    return {
        user: state.user
    };
})(Content);