import React from 'react';
import './App.css';
import Routes from './Routes';
import ApiProvider from './contexts/ApiProvider';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <ApiProvider>
          <Routes />
        </ApiProvider>
      </header>
    </div>
  );
}

export default App;
