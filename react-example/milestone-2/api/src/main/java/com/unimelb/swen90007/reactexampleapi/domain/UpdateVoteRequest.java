package com.unimelb.swen90007.reactexampleapi.domain;

public class UpdateVoteRequest {
    private String name;
    private String email;
    private boolean supporting;
    private Vote.Status status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSupporting() {
        return supporting;
    }

    public void setSupporting(boolean supporting) {
        this.supporting = supporting;
    }

    public Vote.Status getStatus() {
        return status;
    }

    public void setStatus(Vote.Status valid) {
        this.status = valid;
    }
}
