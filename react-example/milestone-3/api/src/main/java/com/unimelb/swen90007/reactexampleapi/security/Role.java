package com.unimelb.swen90007.reactexampleapi.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    ADMIN,
    USER;

    GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(String.format("ROLE_%s", name()));
    }
}
