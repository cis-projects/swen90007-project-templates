import React from 'react';
import PropTypes from 'prop-types';
import styles from './Card.module.css';

export default function Card({ title, subtitle, children }) {
  return (
    <div className={styles.Card}>
      <div className={styles.CardTitle}>{title}</div>
      {(subtitle && <div className={styles.CardSubtitle}>{subtitle}</div>)}
      <hr />
      <div className={styles.CardContent}>
        { children }
      </div>
    </div>
  );
}

Card.propTypes = {
  title: PropTypes.string.isRequired,
  subtitle: PropTypes.string,
  children: PropTypes.node.isRequired,
};

Card.defaultProps = {
  subtitle: null,
};
