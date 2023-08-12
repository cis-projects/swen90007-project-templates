import React from 'react';
import PropTypes from 'prop-types';

function Simple({ aProperty }) {
  return (
    <p>
      {' '}
      some dynamic value:
      {' '}
      {aProperty}
    </p>
  );
}

Simple.propTypes = {
  aProperty: PropTypes.string.isRequired,
};

export default Simple;
