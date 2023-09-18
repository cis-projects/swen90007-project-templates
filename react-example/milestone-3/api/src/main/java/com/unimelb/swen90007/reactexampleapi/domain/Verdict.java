package com.unimelb.swen90007.reactexampleapi.domain;

public class Verdict {
    private final long supporting;
    private final long opposing;

    public Verdict(long supporting, long opposing) {
        this.supporting = supporting;
        this.opposing = opposing;
    }

    public long getSupporting() {
        return supporting;
    }

    public long getOpposing() {
        return opposing;
    }
}
