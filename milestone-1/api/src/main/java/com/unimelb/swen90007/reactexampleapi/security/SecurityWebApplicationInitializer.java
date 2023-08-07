package com.unimelb.swen90007.reactexampleapi.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    public SecurityWebApplicationInitializer() {
        super(WebSecurityConfig.class);
    }
}