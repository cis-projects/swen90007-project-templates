package com.unimelb.swen90007.reactexampleapi.security;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> get(String id);
    void save(RefreshToken token);
    void deleteAllForUsername(String username);
    void delete(String id);
}
