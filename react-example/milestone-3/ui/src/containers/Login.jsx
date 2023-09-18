import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAuthentication } from '../contexts/AuthenticationProvider';
import Error from '../components/Error';
import Button from '../components/Button';
import Input from '../components/Input';
import Page from '../components/Page';

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const { login, authenticationError, authenticating } = useAuthentication();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleUsernameChanged = (event) => {
    event.preventDefault();
    setUsername(event.target.value);
  };

  const handlePasswordChanged = (event) => {
    event.preventDefault();
    setPassword(event.target.value);
  };

  const inputValid = () => username && password;

  const handleLogin = async () => {
    await login(username, password);
    if (authenticationError) {
      return;
    }
    let next = '/';
    if (location.state && location.state.next) {
      next = location.state.next;
    }
    navigate(next);
  };

  return (
    <Page>
      <div>really? prove it 🤨</div>
      {authenticationError
        && <Error messages={[authenticationError]} />}
      <Input label="username" type="text" placeholder="Pepe Roni" value={username} onChange={handleUsernameChanged} required />
      <Input label="password" type="password" placeholder="shhh" value={password} onChange={handlePasswordChanged} required />
      <Button disabled={!inputValid() || authenticating} onClick={handleLogin}>
        Login
      </Button>
    </Page>
  );
}
