import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Page from '../components/Page';
import Button from '../components/Button';
import Error from '../components/Error';
import VoteIcon from '../components/VoteIcon';
import { useApi } from '../contexts/ApiProvider';
import Input from '../components/Input';
import styles from './Vote.module.css';

export default function Vote() {
  const navigate = useNavigate();
  const api = useApi();
  const [inputs, setInputs] = useState({
    email: '',
  });
  const [inputTouched, setInputTouched] = useState(false);
  const [loading, setLoading] = useState();
  const [errors, setErrors] = useState([]);

  const emailValid = (emailValue) => {
    const pattern = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    return pattern.test(emailValue);
  };

  const inputsValid = (inputsValue) => {
    const errorMessages = [];
    if (!inputsValue.email) {
      errorMessages.push('email is required');
    } else if (!emailValid(inputsValue.email)) {
      errorMessages.push('email must be a valid email');
    }
    return errorMessages;
  };

  const handleVoteSubmitted = async (event, voteValue) => {
    event.preventDefault();

    if (loading) {
      return;
    }

    if (!inputTouched) {
      setErrors(['to vote, first enter a valid email']);
      return;
    }

    if (!inputsValid(inputs)) {
      return;
    }

    setLoading(true);
    try {
      await api.submitVote(inputs.name, inputs.email, voteValue);
      navigate('/verdict');
    } catch (e) {
      setErrors([`ouch! we tried submitting your vote but ended up fetching this error instead: ${JSON.stringify(e)}`]);
    } finally {
      setLoading(false);
    }
  };

  const handleInputsChanged = (nextInputs) => {
    setInputTouched(true);
    setInputs(nextInputs);
    setErrors(inputsValid(nextInputs));
  };

  const handleEmailChanged = (event) => {
    event.preventDefault();
    handleInputsChanged({ ...inputs, email: event.target.value });
  };

  const handleNameChanged = (event) => {
    event.preventDefault();
    if (event.target.value) {
      handleInputsChanged({ ...inputs, name: event.target.value });
    } else {
      handleInputsChanged({ ...inputs, name: undefined });
    }
  };

  return (
    <Page>
      <div className={styles.ButtonHolder}>
        <Button
          onClick={(event) => handleVoteSubmitted(event, true)}
        >
          <VoteIcon isSupporting />
        </Button>
        <Button
          onClick={(event) => handleVoteSubmitted(event, false)}
        >
          <VoteIcon />
        </Button>
      </div>
      <div className={styles.Form}>
        {errors && <Error messages={errors} />}
        <Input label="name" type="text" value={inputs.name || ''} placeholder="Pepe Roni" onChange={handleNameChanged} />
        <Input label="email" type="text" className={styles.Input} value={inputs.email} placeholder="pepe.roni@pi.za" onChange={handleEmailChanged} required />
      </div>
    </Page>
  );
}
