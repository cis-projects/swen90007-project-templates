import React from 'react';
import PropTypes from 'prop-types';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuthentication } from '../contexts/AuthenticationProvider';

export default function WithAuthority({ children, authorities }) {
  const { user } = useAuthentication();
  const location = useLocation();

  // if the user is not set yet wait
  if (user === undefined) {
    return null;
  }

  if (!user) {
    const next = location.pathname + location.search + location.hash;
    return <Navigate to="/login" state={{ next }} />;
  }

  if (user.authorities.filter((authority) => authorities.indexOf(authority) !== -1).length === 0) {
    return <Navigate to="/" />;
  }

  return children;
}

WithAuthority.propTypes = {
  children: PropTypes.node.isRequired,
  authorities: PropTypes.arrayOf(PropTypes.string).isRequired,
};
