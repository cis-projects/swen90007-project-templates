import React from 'react';
import PropTypes from 'prop-types';
import styles from './Button.module.css';

function Button({ onClick, disabled, children }) {
  return (
    <button disabled={disabled} className={styles.Button} onClick={onClick} type="button">
      {children}
    </button>
  );
}

Button.propTypes = {
  onClick: PropTypes.func.isRequired,
  disabled: PropTypes.bool,
  children: PropTypes.node.isRequired,
};

Button.defaultProps = {
  disabled: false,
};

export default Button;
