import React from 'react';
import styles from './LoggedInUserDetail.module.css';
import { useAuthentication } from '../contexts/AuthenticationProvider';
import Card from './Card';
import Button from './Button';

function LoggedInUserDetail() {
  const { user, logout } = useAuthentication();

  return (
    user && (
    <div className={styles.LoggedInUserDetail}>
      <Card title={`Hi ${user.username || 'you'}`}>
        <Button onClick={() => logout()}>Log out</Button>
      </Card>
    </div>
    )
  );
}

export default LoggedInUserDetail;
