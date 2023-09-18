package com.unimelb.swen90007.reactexampleapi.port.postgres;

import com.unimelb.swen90007.reactexampleapi.security.RefreshToken;
import com.unimelb.swen90007.reactexampleapi.security.RefreshTokenRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class PostgresRefreshTokenRepository implements RefreshTokenRepository {

    private final ConnectionProvider connectionProvider;

    public PostgresRefreshTokenRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<RefreshToken> get(String id) {
        RefreshToken token = null;
        var connection = connectionProvider.nextConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, token_id, username FROM app.refresh_token WHERE id=?"
            );
            statement.setObject(1, UUID.fromString(id));
            var results = statement.executeQuery();
            if (results.next()) {
                token = new RefreshToken();
                token.setId(results.getObject("id", UUID.class).toString());
                token.setTokenId(results.getString("token_id"));
                token.setUsername(results.getString("username"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("failed to get refresh token [%s]: %s", id, e.getMessage()), e);
        } finally {
            connectionProvider.releaseConnection(connection);
        }
        return Optional.ofNullable(token);
    }

    @Override
    public void save(RefreshToken token) {
        var connection = connectionProvider.nextConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO app.refresh_token (id, token_id, username) VALUES (?, ?, ?)"
            );
            statement.setObject(1, UUID.fromString(token.getId()));
            statement.setString(2, token.getTokenId());
            statement.setString(3, token.getUsername());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("failed to create refresh token [%s]: %s", token.getTokenId(), e.getMessage()), e);
        } finally {
            connectionProvider.releaseConnection(connection);
        }
    }

    @Override
    public void deleteAllForUsername(String username) {
        var connection = connectionProvider.nextConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM app.refresh_token WHERE username=?"
            );
            statement.setString(1, username);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("failed to delete refresh tokens for username [%s]: %s", username, e.getMessage()), e);
        } finally {
            connectionProvider.releaseConnection(connection);
        }
    }

    @Override
    public void delete(String id) {
        var connection = connectionProvider.nextConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM app.refresh_token WHERE id=?"
            );
            statement.setObject(1, UUID.fromString(id));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("failed to delete refresh token [%s]: %s", id, e.getMessage()), e);
        } finally {
            connectionProvider.releaseConnection(connection);
        }
    }
}
