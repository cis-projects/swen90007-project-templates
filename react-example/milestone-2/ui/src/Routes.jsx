import React from 'react';
import {
  BrowserRouter, Routes, Route, Navigate,
} from 'react-router-dom';
import Home from './containers/Home';
import Vote from './containers/Vote';
import Verdict from './containers/Verdict';
import Votes from './containers/Votes';

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/vote" element={<Vote />} />
        <Route path="/admin/votes" element={<Votes />} />
        <Route path="/verdict" element={<Verdict />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;
