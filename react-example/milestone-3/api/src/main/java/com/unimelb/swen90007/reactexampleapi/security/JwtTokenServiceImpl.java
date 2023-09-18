package com.unimelb.swen90007.reactexampleapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JwtTokenServiceImpl implements TokenService {

    private static final String CLAIM_AUTHORITIES = "authorities";
    private static final String CLAIM_USERNAME = "username";
    private final String secret;
    private SecretKey key;
    private final int timeToLiveSeconds;
    private final String issuer;
    private final RefreshTokenRepository repository;

    public JwtTokenServiceImpl(String secret, int timeToLiveSeconds, String issuer, RefreshTokenRepository repository) {
        this.secret = secret;
        this.timeToLiveSeconds = timeToLiveSeconds;
        this.issuer = issuer;
        this.repository = repository;
    }

    @Override
    public UsernamePasswordAuthenticationToken readToken(String accessToken) {
        try {
            var jws = parse(accessToken);
            return new UsernamePasswordAuthenticationToken(getUsername(jws.getBody()), null, getAuthorities(jws.getBody()));
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("token expired");
        } catch (JwtException e) {
            throw new BadCredentialsException("bad token");
        }
    }

    @Override
    public Token createToken(UserDetails user) {
        return generateToken(user.getUsername(), user.getAuthorities());
    }

    @Override
    public Token refresh(String accessToken, String refreshTokenId) {
        var claims = parseExpired(accessToken);
        var refresh = repository.get(refreshTokenId)
                .orElseThrow(() -> new BadCredentialsException("bad refresh token"));
        if (!refresh.getTokenId().equals(claims.getId())) {
            throw new BadCredentialsException("bad refresh token");
        }
        repository.delete(refreshTokenId);
        return generateToken(getUsername(claims), getAuthorities(claims));
    }

    @Override
    public void logout(String username) {
        repository.deleteAllForUsername(username);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
        var claimedAuthorities = (List<String>) claims.get(CLAIM_AUTHORITIES, List.class);
        return claimedAuthorities.stream().map(SimpleGrantedAuthority::new).toList();
    }

    private String getUsername(Claims claims) {
        return claims.get(CLAIM_USERNAME, String.class);
    }

    private Token generateToken(String username, Collection<? extends GrantedAuthority> authorities) {

        var now = new Date();
        var expires = Date.from(now.toInstant().plusSeconds(timeToLiveSeconds));
        var id = UUID.randomUUID().toString();
        var tokenStr = Jwts.builder()
                .setIssuer(issuer)
                .setSubject("swen90007-template-api")
                .setAudience(issuer)
                .setExpiration(expires)
                .setNotBefore(now)
                .setIssuedAt(now)
                .setId(id)
                .claim(CLAIM_USERNAME, username)
                .claim(CLAIM_AUTHORITIES, authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .signWith(getKey())
                .compact();

        var refreshToken = new RefreshToken();
        refreshToken.setId(UUID.randomUUID().toString());
        refreshToken.setTokenId(id);
        refreshToken.setUsername(username);
        repository.save(refreshToken);

        var token = new Token();
        token.setAccessToken(tokenStr);
        token.setRefreshTokenId(refreshToken.getId());
        return token;
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .requireAudience(this.issuer)
                .build()
                .parseClaimsJws(token);
    }

    private Claims parseExpired(String token) {
        try {
            return parse(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private SecretKey getKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }
}
