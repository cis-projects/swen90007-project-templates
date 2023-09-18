import React from 'react';
import PropTypes from 'prop-types';
import styles from './Error.module.css';

function Error({ messages }) {
  return (
    <ul className={styles.Error}>
      {
        (messages && messages.map((message) => <li key={message}>{message}</li>))
        || <li>{'ouch! we tried, but it wasn\'t enough ...'}</li>
      }
    </ul>
  );
}

Error.propTypes = {
  messages: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default Error;
