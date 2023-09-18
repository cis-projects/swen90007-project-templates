package com.unimelb.swen90007.reactexampleapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimelb.swen90007.reactexampleapi.domain.VoteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "verdict", urlPatterns = "/verdict")
public class VerdictResource extends HttpServlet {

    private VoteService voteService;
    private ObjectMapper mapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        MarshallingRequestHandler.of(mapper, resp, ErrorHandler.of(
                () -> ResponseEntity.ok(voteService.calculateVerdict())))
                .handle();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        voteService = (VoteService) getServletContext().getAttribute(VoteContextListener.VOTE_SERVICE);
        mapper = (ObjectMapper) getServletContext().getAttribute(VoteContextListener.MAPPER);
     }
}
