package com.unimelb.swen90007.reactexampleapi.domain;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String type, String id) {
        this(type, id, null);
    }

    public NotFoundException(String type, String id, Throwable cause) {
        super(String.format("%s with ID %s not found", type, id), cause);
    }
}
