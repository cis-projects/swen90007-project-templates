package com.unimelb.swen90007.reactexampleapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MarshallingRequestHandler implements RequestHandler {

    private final RequestHandler handler;
    private final ObjectMapper mapper;
    private final HttpServletResponse resp;


    public MarshallingRequestHandler(ObjectMapper mapper, HttpServletResponse resp, RequestHandler handler) {
        this.mapper = mapper;
        this.resp = resp;
        this.handler = handler;
    }

    @Override
    public ResponseEntity handle() {
            var entity = handler.handle();
            try {
                if (entity.getBody().isPresent()) {
                    resp.getWriter().append(mapper.writeValueAsString(entity.getBody().get()));
                    resp.setContentType("application/json");
                }
                resp.setStatus(entity.getStatus());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            return entity;
    }

    static MarshallingRequestHandler of(ObjectMapper mapper, HttpServletResponse resp, RequestHandler handler) {
        return new MarshallingRequestHandler(mapper, resp, handler);
    }
}
