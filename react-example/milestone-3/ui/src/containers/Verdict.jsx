import React, { useState, useEffect } from 'react';
import Page from '../components/Page';
import VoteIcon from '../components/VoteIcon';
import { useApi } from '../contexts/ApiProvider';
import Error from '../components/Error';
import styles from './Verdict.module.css';

export default function Verdict() {
  const api = useApi();
  const [verdict, setVerdict] = useState({
    supporting: '?',
    opposing: '?',
  });
  const [loaded, setLoaded] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const controller = new AbortController();
    const { signal } = controller;
    const doFetch = async () => {
      try {
        setVerdict(await api.getVerdict(signal));
      } catch (e) {
        if (e?.name !== 'AbortError') {
          setError(`ouch! we tried to fetch the votes but ended up fetching this error instead: ${JSON.stringify(e)}`);
        }
      } finally {
        setLoaded(true);
      }
    };
    doFetch();
    return () => controller.abort();
  }, []);

  return (
    <Page>
      {error && <Error messages={[error]} />}
      {loaded && (
        <div className={styles.Verdict}>
          <div>
            <div className={styles.Icon}>
              <VoteIcon isSupporting />
            </div>
            <div>{verdict?.supporting}</div>
          </div>
          <div>
            <div className={styles.Icon}>
              <VoteIcon />
            </div>
            <div>{verdict?.opposing}</div>
          </div>
        </div>
      )}
    </Page>
  );
}
