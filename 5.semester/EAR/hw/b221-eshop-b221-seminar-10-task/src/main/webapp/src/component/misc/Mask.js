import React from 'react';
import PropTypes from 'prop-types';
import {ClipLoader} from 'react-spinners';
import './Mask.css';

const Mask = (props) => {
    return <div className='mask'>
        <div className='spinner-container'>
            <div style={{width: 32, height: 32, margin: 'auto'}}>
                <ClipLoader color='#337ab7' size={32}/>
            </div>
            <div className='spinner-message'>{props.text}</div>
        </div>
    </div>;
};

Mask.propTypes = {
    text: PropTypes.string
};

Mask.defaultProps = {
    text: 'Please wait'
};

export default Mask;