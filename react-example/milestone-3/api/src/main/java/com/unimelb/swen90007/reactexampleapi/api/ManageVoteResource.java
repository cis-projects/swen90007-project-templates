package com.unimelb.swen90007.reactexampleapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimelb.swen90007.reactexampleapi.domain.UpdateVoteRequest;
import com.unimelb.swen90007.reactexampleapi.domain.ValidationException;
import com.unimelb.swen90007.reactexampleapi.domain.VoteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;
import java.util.regex.Pattern;

@WebServlet(name = "manageVote", urlPatterns = "/manage/vote/*")
public class ManageVoteResource extends HttpServlet {

    private static final Pattern VOTE_ID_PATTERN = Pattern.compile("^/(.*)");

    private VoteService voteService;
    private ObjectMapper mapper;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        MarshallingRequestHandler.of(mapper, resp, ErrorHandler.of(
                () -> {
                    var maybeVoteId = getVoteId(req.getPathInfo());
                    if (maybeVoteId.isPresent()) {
                        try {
                            StringWriter writer = new StringWriter();
                            req.getReader().transferTo(writer);
                            var update = mapper.readValue(writer.toString(), UpdateVoteRequest.class);
                            return ResponseEntity.ok(voteService.updateVote(maybeVoteId.get(), update));
                        } catch (IOException e) {
                            throw new ValidationException(e.getMessage(), e);
                        }
                    } else {
                        throw new ValidationException("invalid vote ID in path");
                    }
                }))
                .handle();
    }

    private Optional<String> getVoteId(String path) {
        var matcher = VOTE_ID_PATTERN.matcher(path);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        voteService = (VoteService) getServletContext().getAttribute(VoteContextListener.VOTE_SERVICE);
        mapper = (ObjectMapper) getServletContext().getAttribute(VoteContextListener.MAPPER);
    }
}

