package com.unimelb.swen90007.reactexampleapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimelb.swen90007.reactexampleapi.domain.ValidationException;
import com.unimelb.swen90007.reactexampleapi.domain.VoteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "manageVotes", urlPatterns = "/manage/vote")
public class ManageVotesResource extends HttpServlet {

    private VoteService voteService;
    private ObjectMapper mapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        MarshallingRequestHandler.of(mapper, resp, ErrorHandler.of(
                        () -> {
                            long offset = validateNumberParameter(req, "offset", 0);
                            long limit = validateNumberParameter(req, "limit", 20);
                            return ResponseEntity.ok(voteService.getAllVotes(offset, limit));
                        }))
                .handle();
    }

    private long validateNumberParameter(HttpServletRequest req, String parameterName, long defaultValue) throws ValidationException {
        long value = defaultValue;
        try {
            String valueObj = req.getParameter(parameterName);
            if (valueObj != null) {
                value = Long.parseLong(valueObj);
            }
        } catch (NumberFormatException e) {
            throw new ValidationException(String.format("%s must be a number", parameterName));
        }
        return value;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        voteService = (VoteService) getServletContext().getAttribute(VoteContextListener.VOTE_SERVICE);
        mapper = (ObjectMapper) getServletContext().getAttribute(VoteContextListener.MAPPER);
    }
}
