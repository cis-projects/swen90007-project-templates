package com.unimelb.swen90007.reactexampleapi.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class VoteService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");

    private final VoteRepository repository;

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public Vote submit(NewVoteRequest request) {
        if (request.getEmail() == null) {
            throw new ValidationException("email is required");
        } else if (!isEmailValid(request.getEmail())) {
            throw new ValidationException("email must be a valid email");
        }

        var vote = new Vote();
        vote.setId(UUID.randomUUID().toString());
        vote.setName(request.getName());
        vote.setEmail(request.getEmail());
        vote.setSupporting(request.isSupporting());
        vote.setStatus(Vote.Status.UNVERIFIED);
        vote.setCreated(OffsetDateTime.now());
        repository.save(vote);
        return vote;
    }

    public Verdict calculateVerdict() {
        var votes = repository.getValid();
        var supporting = votes.stream()
                .filter(Vote::isSupporting)
                .count();

        return new Verdict(
                supporting,
                votes.size() - supporting
        );
    }

    public List<Vote> getAllVotes(long offset, long limit) {
        return repository.getAll(offset, limit);
    }

    public Optional<Vote> getVoteById(String id) {
        return repository.get(id);
    }

    public Vote updateVote(String id, UpdateVoteRequest request) {
        var vote = repository.get(id).orElseThrow(() -> new NotFoundException("vote", id));
        if (request.getEmail() == null) {
            throw new ValidationException("email is required");
        } else if (!isEmailValid(request.getEmail())) {
            throw new ValidationException("email must be a valid email");
        }
        vote.setName(request.getName());
        vote.setEmail(request.getEmail());
        vote.setStatus(request.getStatus());
        vote = repository.save(vote);
        return vote;
    }

    private boolean isEmailValid(String email) {
        return EMAIL_PATTERN.matcher(email).find();
    }
}
