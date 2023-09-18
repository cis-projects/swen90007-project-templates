import React from 'react';
import './App.css';
import Routes from './Routes';
import ApiProvider from './contexts/ApiProvider';
import AuthenticationProvider from './contexts/AuthenticationProvider';
import LoggedInUserDetail from './components/LoggedInUserDetail';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <ApiProvider>
          <AuthenticationProvider>
            <LoggedInUserDetail />
            <Routes />
          </AuthenticationProvider>
        </ApiProvider>
      </header>
    </div>
  );
}

export default App;
