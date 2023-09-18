import React, { useState, forwardRef } from 'react';
import PropTypes from 'prop-types';
import { useApi } from '../contexts/ApiProvider';
import Error from '../components/Error';
import VoteIcon from '../components/VoteIcon';
import Card from '../components/Card';
import Button from '../components/Button';

function ManageVoteCard({
  id, name, email, supporting, status,
}, ref) {
  const [nextStatus, setNextStatus] = useState(status);
  const [errors, setErrors] = useState([]);
  const [loading, setLoading] = useState(false);
  const api = useApi();

  const handleButtonClicked = async (requestedStatus) => {
    try {
      setLoading(true);
      await api.updateVote({
        id, name, email, supporting, status: requestedStatus,
      });
      setNextStatus(requestedStatus);
    } catch (e) {
      setErrors([JSON.stringify(e)]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div ref={ref} hidden={nextStatus !== 'UNVERIFIED'}>
      <Card title={name} subtitle={email}>
        <div>voted:</div>
        <VoteIcon isSupporting={supporting} size="small" />
        {errors && <Error messages={errors} />}
        {!loading && (nextStatus === 'UNVERIFIED' || nextStatus === 'REJECTED')
          && (
          <Button onClick={() => handleButtonClicked('ACCEPTED')}>
            Accept
          </Button>
          )}
        {!loading && (nextStatus === 'UNVERIFIED' || nextStatus === 'ACCEPTED')
          && (
          <Button onClick={() => handleButtonClicked('REJECTED')}>
            Reject
          </Button>
          )}
      </Card>
    </div>
  );
}

export default forwardRef(ManageVoteCard);

ManageVoteCard.defaultProps = {
  name: 'Anonymous',
};

ManageVoteCard.propTypes = {
  id: PropTypes.string.isRequired,
  name: PropTypes.string,
  email: PropTypes.string.isRequired,
  supporting: PropTypes.bool.isRequired,
  status: PropTypes.oneOf(['UNVERIFIED', 'ACCEPTED', 'REJECTED']).isRequired,
};
