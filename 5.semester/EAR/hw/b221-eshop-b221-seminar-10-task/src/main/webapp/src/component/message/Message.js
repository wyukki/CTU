import * as React from 'react';
import {Alert} from "reactstrap";
import {connect} from 'react-redux';
import {dismissMessage} from "../../action/SyncActions";
import Constants from '../../util/Constants';

export class Message extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            open: true
        };
    }

    componentDidMount() {
        this.timer = setTimeout(() => {
            this._toggleAlert();
        }, Constants.MESSAGE_DISPLAY_TIMEOUT);
    }

    componentWillUnmount() {
        clearTimeout(this.timer);
    }

    _toggleAlert = () => {
        this.setState({open: false});
        this.props.dismissMessage(this.props.message);
    };

    render() {
        const message = this.props.message;
        return <Alert color={message.type} isOpen={this.state.open}
                      toggle={this._toggleAlert}>{message.message}</Alert>;
    }
}

export default connect(null, (dispatch) => {
    return {
        dismissMessage: (message) => dispatch(dismissMessage(message))
    }
})(Message);