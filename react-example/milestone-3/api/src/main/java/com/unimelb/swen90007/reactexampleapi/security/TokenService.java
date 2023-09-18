package com.unimelb.swen90007.reactexampleapi.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    UsernamePasswordAuthenticationToken readToken(String accessToken);
    Token createToken(UserDetails user);
    Token refresh(String accessToken, String refreshTokenId);
    void logout(String username);
}
