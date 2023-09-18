package com.unimelb.swen90007.reactexampleapi.domain;

public class NewVoteRequest {
    private String name;
    private String email;
    private boolean supporting;

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
}
