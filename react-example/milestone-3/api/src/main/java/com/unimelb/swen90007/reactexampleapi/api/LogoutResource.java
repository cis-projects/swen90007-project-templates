package com.unimelb.swen90007.reactexampleapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimelb.swen90007.reactexampleapi.domain.ValidationException;
import com.unimelb.swen90007.reactexampleapi.security.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.StringWriter;

@WebServlet(name = "logout", urlPatterns = "/auth/logout")
public class LogoutResource extends HttpServlet {
    private ObjectMapper mapper;
    private TokenService jwtTokenService;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MarshallingRequestHandler.of(mapper, resp, ErrorHandler.of(() -> {
            try {
                var bodyBuffer = new StringWriter();
                req.getReader().transferTo(bodyBuffer);
                var logout = mapper.readValue(bodyBuffer.toString(), LogoutRequest.class);
                jwtTokenService.logout(logout.getUsername());
                return ResponseEntity.ok(null);
            } catch (IOException e) {
                throw new ValidationException(String.format("invalid logout body: %s", e.getMessage()));
            }
        })).handle();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        jwtTokenService = (TokenService) getServletContext().getAttribute(VoteContextListener.TOKEN_SERVICE);
        mapper = (ObjectMapper) getServletContext().getAttribute(VoteContextListener.MAPPER);
    }
}
