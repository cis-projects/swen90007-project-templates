import React from 'react';
import { Link } from 'react-router-dom';
import Page from '../components/Page';

function Home() {
  return (
    <Page>
      <Link to="/vote">submit a vote</Link>
      <br />
      <Link to="/verdict">take me to the votes</Link>
      <br />
      <Link to="/admin/votes">I&apos;m an admin</Link>
    </Page>
  );
}

export default Home;
