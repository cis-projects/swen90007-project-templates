package com.unimelb.swen90007.reactexampleapi.port.postgres;


import com.unimelb.swen90007.reactexampleapi.domain.Vote;
import com.unimelb.swen90007.reactexampleapi.domain.VoteRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostgresVoteRepository implements VoteRepository {

    private final ConnectionProvider connectionProvider;

    public PostgresVoteRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Vote save(Vote vote) {
        if (vote.isNew()) {
            return insert(vote);
        }
        return update(vote);
    }

    private Vote insert(Vote vote) {
        var connection = connectionProvider.nextConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO app.vote (id, name, email, supporting, status, created) VALUES (?, ?, ?, ?, ?, ?)"
            );
            statement.setObject(1, UUID.fromString(vote.getId()));
            statement.setString(2, vote.getName());
            statement.setString(3, vote.getEmail());
            statement.setBoolean(4, vote.isSupporting());
            statement.setString(5, vote.getStatus().name());
            statement.setObject(6, vote.getCreated());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(String.format("failed to insert new vote: %s", e.getMessage()), e);
        } finally {
            vote.setNew(false);
            connectionProvider.releaseConnection(connection);
        }
        return vote;
    }

    private Vote update(Vote vote) {
        var connection = connectionProvider.nextConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE app.vote SET name=?, email=?, supporting=?, status=?, created=? WHERE id=?"
            );
            statement.setString(1, vote.getName());
            statement.setString(2, vote.getEmail());
            statement.setBoolean(3, vote.isSupporting());
            statement.setString(4, vote.getStatus().name());
            statement.setObject(5, vote.getCreated());
            statement.setObject(6, UUID.fromString(vote.getId()));
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(String.format("failed to insert new vote: %s", e.getMessage()), e);
        } finally {
            connectionProvider.releaseConnection(connection);
        }
        return vote;
    }

    @Override
    public Optional<Vote> get(String id) {
        Vote vote = null;
        var connection = connectionProvider.nextConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, name, email, supporting, status, created FROM app.vote WHERE id=?"
            );
            statement.setObject(1, UUID.fromString(id));
            var results = statement.executeQuery();
            if (results.next()) {
                vote = map(results);
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("failed to get vote [%s]: %s", id, e.getMessage()), e);
        } finally {
            connectionProvider.releaseConnection(connection);
        }
        return Optional.ofNullable(vote);
    }

    @Override
    public List<Vote> getValid() {
        var votes = new ArrayList<Vote>();
        var connection = connectionProvider.nextConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, email, name, supporting, status, created FROM app.vote WHERE status!='REJECTED' ORDER BY created"
            );
            var results = statement.executeQuery();
            while (results.next()) {
                votes.add(map(results));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("failed to get votes: %s", e.getMessage()), e);
        } finally {
            connectionProvider.releaseConnection(connection);
        }
        return votes;
    }

    @Override
    public List<Vote> getAll(long offset, long limit) {
        var votes = new ArrayList<Vote>();
        var connection = connectionProvider.nextConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, email, name, supporting, status, created FROM app.vote ORDER BY created LIMIT ? OFFSET ?"
            );
            statement.setLong(1, limit);
            statement.setLong(2, offset);
            var results = statement.executeQuery();
            while (results.next()) {
                votes.add(map(results));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("failed to get votes: %s", e.getMessage()), e);
        } finally {
            connectionProvider.releaseConnection(connection);
        }
        return votes;
    }

    private Vote map(ResultSet resultSet) throws SQLException {
        var vote = new Vote();
        vote.setNew(false);
        vote.setId(resultSet.getString("id"));
        vote.setName(resultSet.getString("name"));
        vote.setEmail(resultSet.getString("email"));
        vote.setSupporting(resultSet.getBoolean("supporting"));
        vote.setStatus(Vote.Status.valueOf(resultSet.getString("status")));
        vote.setCreated(resultSet.getObject("created", OffsetDateTime.class));
        return vote;
    }
}
