import * as React from 'react';
import {connect} from "react-redux";
import Constants from '../../util/Constants';
import Message from "./Message";
import './Messages.css';

export const Messages = (props) => {
    const count = props.messages.length < Constants.MESSAGE_DISPLAY_COUNT ? props.messages.length : Constants.MESSAGE_DISPLAY_COUNT;
    const toRender = props.messages.slice(0, count);
    return <div className={'message-container messages-' + count}>
        {toRender.map((m, i) => <Message key={i} message={m}/>)}
    </div>
};

export default connect((state) => {
    return {
        messages: state.messages
    };
})(Messages);