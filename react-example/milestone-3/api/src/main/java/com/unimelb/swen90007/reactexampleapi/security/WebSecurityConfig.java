package com.unimelb.swen90007.reactexampleapi.security;

import com.unimelb.swen90007.reactexampleapi.api.VoteContextListener;
import jakarta.servlet.ServletContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements ServletContextAware {
    private static final RequestMatcher PROTECTED_URLS = new AntPathRequestMatcher("/manage/vote");
    private static final String PROPERTY_ADMIN_USERNAME = "admin.username";
    private static final String PROPERTY_ADMIN_PASSWORD = "admin.password";
    private static final String PROPERTY_CORS_ORIGINS_UI = "cors.origins.ui";

    private TokenService jwtTokenService;
    private ServletContext servletContext;

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var userDetailsService = new InMemoryUserDetailsManager();
        userDetailsService.createUser(adminUser(passwordEncoder));
        userDetailsService.createUser(nonAdminUser(passwordEncoder)); // for testing purposes
        servletContext.setAttribute(VoteContextListener.USER_DETAILS_SERVICE, userDetailsService);
        return userDetailsService;
    }

    @Bean
    AuthenticationEntryPoint unauthorizedEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService, TokenAuthenticationProvider authenticationProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .and()
                .authenticationProvider(authenticationProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, TokenAuthenticationFilter tokenAuthenticationFilter, AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        return http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(authenticationEntryPoint, PROTECTED_URLS)
                .and()
                .addFilterBefore(tokenAuthenticationFilter, AnonymousAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PROTECTED_URLS)
                            .hasRole(Role.ADMIN.name())
                        .anyRequest()
                            .permitAll())
                .authenticationManager(authenticationManager)
                .cors(Customizer.withDefaults())
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .build();
    }

    @Bean
    public TokenAuthenticationProvider tokenAuthenticationProvider(UserDetailsService userDetailsService) {
        return new TokenAuthenticationProvider(userDetailsService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(System.getProperty(PROPERTY_CORS_ORIGINS_UI)));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy((request, response, url) -> {});
        return successHandler;
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(AuthenticationManager authenticationManager, SimpleUrlAuthenticationSuccessHandler simpleUrlAuthenticationSuccessHandler) {
        var filter = new TokenAuthenticationFilter(PROTECTED_URLS, jwtTokenService);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(simpleUrlAuthenticationSuccessHandler);
        return filter;
    }

    private UserDetails adminUser(PasswordEncoder passwordEncoder) {
        return new User(System.getProperty(PROPERTY_ADMIN_USERNAME),
                passwordEncoder.encode(System.getProperty(PROPERTY_ADMIN_PASSWORD)),
                Collections.singleton(Role.ADMIN.toAuthority()));
    }

    private UserDetails nonAdminUser(PasswordEncoder passwordEncoder) {
        return new User("user",
                passwordEncoder.encode("user"),
                Collections.singleton(Role.USER.toAuthority()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        var passwordEncoder = new BCryptPasswordEncoder();
        servletContext.setAttribute(VoteContextListener.PASSWORD_ENCODER, passwordEncoder);
        return passwordEncoder;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        jwtTokenService = (TokenService) servletContext.getAttribute(VoteContextListener.TOKEN_SERVICE);
    }
}