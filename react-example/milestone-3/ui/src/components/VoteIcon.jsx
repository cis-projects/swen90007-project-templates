import React from 'react';
import PropTypes from 'prop-types';
import styles from './VoteIcon.module.css';

function VoteIcon({ isSupporting, size }) {
  return <div className={[styles.Icon, styles[size]].join(' ')}>{(isSupporting && '🤤') || '🤢'}</div>;
}

VoteIcon.propTypes = {
  isSupporting: PropTypes.bool,
  size: PropTypes.oneOf(['large', 'small']),
};

VoteIcon.defaultProps = {
  isSupporting: false,
  size: 'large',
};

export default VoteIcon;
