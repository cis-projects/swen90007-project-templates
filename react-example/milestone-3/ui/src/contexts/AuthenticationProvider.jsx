import React, {
  createContext, useContext, useState, useMemo, useEffect,
} from 'react';
import PropType from 'prop-types';
import { useApi } from './ApiProvider';

const STORAGE_ITEM_TOKEN = 'accessToken';

const AuthenticationContext = createContext();

export default function AuthenticationProvider({ children }) {
  const api = useApi();
  const [user, setUser] = useState();
  const [authenticating, setAuthenticating] = useState(false);
  const [authenticationError, setAuthenticationError] = useState();

  const extractUserFromToken = (token) => JSON.parse(atob(token.split('.')[1]));

  const login = async (username, password) => {
    setAuthenticating(true);
    setAuthenticationError(undefined);
    try {
      const token = await api.login(username, password);
      setUser(extractUserFromToken(token.accessToken));
    } catch (e) {
      setAuthenticationError(JSON.stringify(e));
    } finally {
      setAuthenticating(false);
    }
  };

  const logout = async () => {
    if (user) {
      await api.logout(user.username);
      setAuthenticationError(undefined);
      setAuthenticating(false);
      setUser(null);
    }
  };

  useEffect(() => {
    const token = localStorage.getItem(STORAGE_ITEM_TOKEN);
    if (token) {
      setAuthenticating(true);
      try {
        setUser(extractUserFromToken(token));
      } catch (e) {
        setAuthenticationError(JSON.stringify(e));
        setUser(null);
      } finally {
        setAuthenticating(false);
      }
    } else {
      setUser(null);
    }
  }, []);

  const value = useMemo(
    () => ({
      user, login, logout, authenticationError, authenticating,
    }),
    [user, authenticationError, authenticating],
  );

  return (
    <AuthenticationContext.Provider
      value={value}
    >
      {children}
    </AuthenticationContext.Provider>
  );
}

export function useAuthentication() {
  return useContext(AuthenticationContext);
}

AuthenticationProvider.propTypes = {
  children: PropType.node.isRequired,
};
