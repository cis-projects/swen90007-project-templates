import React from 'react';
import {
  BrowserRouter, Routes, Route, Navigate,
} from 'react-router-dom';
import Home from './containers/Home';
import Vote from './containers/Vote';
import Verdict from './containers/Verdict';
import Login from './containers/Login';
import Votes from './containers/Votes';
import WithAuthority from './hoc/withAuthority';

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/vote" element={<Vote />} />
        <Route
          path="/admin/votes"
          element={(
            <WithAuthority authorities={['ROLE_ADMIN']}>
              <Votes />
            </WithAuthority>
          )}
        />
        <Route path="/verdict" element={<Verdict />} />
        <Route path="/login" element={<Login />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;
