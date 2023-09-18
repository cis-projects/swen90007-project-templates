package com.unimelb.swen90007.reactexampleapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimelb.swen90007.reactexampleapi.domain.NewVoteRequest;
import com.unimelb.swen90007.reactexampleapi.domain.ValidationException;
import com.unimelb.swen90007.reactexampleapi.domain.VoteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.StringWriter;

@WebServlet(name = "votes", urlPatterns = "/vote")
public class VotesResource extends HttpServlet {

    private VoteService voteService;
    private ObjectMapper mapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MarshallingRequestHandler.of(mapper, resp, ErrorHandler.of(
                () -> {
                    try {
                        StringWriter writer = new StringWriter();
                        req.getReader().transferTo(writer);
                        var newVote = mapper.readValue(writer.toString(), NewVoteRequest.class);
                        var vote = voteService.submit(newVote);
                        return ResponseEntity.of(HttpServletResponse.SC_CREATED, vote);
                    } catch (IOException e) {
                        throw new ValidationException(String.format("invalid new vote body: %s", e.getMessage()));
                    }
                }))
                .handle();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        voteService = (VoteService) getServletContext().getAttribute(VoteContextListener.VOTE_SERVICE);
        mapper = (ObjectMapper) getServletContext().getAttribute(VoteContextListener.MAPPER);
    }
}
