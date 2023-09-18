import React from 'react';
import PropTypes from 'prop-types';
import styles from './Page.module.css';

function Page({ children }) {
  return (
    <div className={styles.Page}>
      <h1>🍕 + 🍍???</h1>
      {children}
    </div>
  );
}

Page.propTypes = {
  children: PropTypes.node.isRequired,
};

export default Page;
