package com.unimelb.swen90007.reactexampleapi.domain;

import java.util.List;
import java.util.Optional;

public interface VoteRepository {
    Vote save(Vote vote);

    Optional<Vote> get(String id);

    List<Vote> getValid();

    List<Vote> getAll(long offset, long limit);

}
