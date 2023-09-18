package com.unimelb.swen90007.reactexampleapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.OffsetDateTime;

public class Vote {
    private String id;
    private String name;
    private String email;
    private boolean supporting;
    private Status status;
    private OffsetDateTime created;
    private boolean _new = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    @JsonIgnore
    public boolean isNew() {
        return _new;
    }

    @JsonIgnore
    public void setNew(boolean _new) {
        this._new = _new;
    }

    public static enum Status {
        UNVERIFIED,
        ACCEPTED,
        REJECTED
    }
}
