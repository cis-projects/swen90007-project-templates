import React from 'react';
import PropTypes from 'prop-types';
import styles from './Input.module.css';

export default function Input({
  label, onChange, type, value, placeholder, required,
}) {
  return (
    <label className={styles.Label} htmlFor={`${label}Input`}>
      {label}
      {required && <span className={styles.Required}>*</span>}
      <input id={`${label}Input`} type={type} className={styles.Input} value={value} placeholder={placeholder} onChange={onChange} />
    </label>
  );
}

Input.propTypes = {
  label: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  type: PropTypes.oneOf(['text', 'password']),
  value: PropTypes.string.isRequired,
  placeholder: PropTypes.string,
  required: PropTypes.bool,
};

Input.defaultProps = {
  type: 'text',
  placeholder: '',
  required: false,
};
