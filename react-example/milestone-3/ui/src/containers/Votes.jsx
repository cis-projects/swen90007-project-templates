import React, { useState, useEffect } from 'react';
import { useApi } from '../contexts/ApiProvider';
import Error from '../components/Error';
import ManageVoteCard from './ManageVoteCard';
import styles from './Votes.module.css';

const PAGE_SIZE = 20;

export default function Votes() {
  const [votes, setVotes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState([]);
  const [page, setPage] = useState(0);
  const [allLoaded, setAllLoaded] = useState(false);
  const api = useApi();

  const fetchVotes = async (signal) => {
    try {
      setLoading(true);
      const nextVotes = await api.getVotes(page * PAGE_SIZE, PAGE_SIZE, signal);
      if (nextVotes.length < PAGE_SIZE) {
        setAllLoaded(true);
      }
      setVotes([...votes, ...nextVotes]);
      setPage(page + 1);
    } catch (e) {
      if (e?.name !== 'AbortError') {
        setErrors([JSON.stringify(e)]);
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const controller = new AbortController();
    const { signal } = controller;
    fetchVotes(signal);
    return () => controller.abort();
  }, []);

  const handlePagination = async (event) => {
    if (event.target.offsetHeight + event.target.scrollTop >= event.target.scrollHeight
      && !loading && !allLoaded) {
      await fetchVotes();
    }
  };

  return (
    <div className={styles.Votes} onScroll={handlePagination}>
      {!loading && <Error messages={errors} />}
      <ul>
        {votes.filter(({ status }) => status === 'UNVERIFIED').map(({
          id, name, email, supporting, status,
        }) => (
          <li key={id}>
            <ManageVoteCard
              id={id}
              name={name}
              email={email}
              supporting={supporting}
              status={status}
            />
          </li>
        ))}
      </ul>
    </div>
  );
}
