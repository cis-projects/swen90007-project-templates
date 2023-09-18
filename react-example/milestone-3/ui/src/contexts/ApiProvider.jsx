import React, { createContext, useContext, useMemo } from 'react';
import PropType from 'prop-types';
import Api from '../api';

const ApiContext = createContext();

export default function ApiProvider({ children }) {
  const api = useMemo(() => new Api(process.env.REACT_APP_API_BASE_URL), []);

  return (
    <ApiContext.Provider value={api}>
      {children}
    </ApiContext.Provider>
  );
}

export function useApi() {
  return useContext(ApiContext);
}

ApiProvider.propTypes = {
  children: PropType.node.isRequired,
};
