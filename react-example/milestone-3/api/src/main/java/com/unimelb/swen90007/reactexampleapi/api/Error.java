package com.unimelb.swen90007.reactexampleapi.api;

class Error {
    private int status;
    private String message;
    private String reason;

    public Error(int status, String message, String reason) {
        this.status = status;
        this.message = message;
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static ErrorBuilder builder() {
        return new ErrorBuilder();
    }

    public static class ErrorBuilder {
        private int status;
        private String message;
        private String reason;

        public ErrorBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ErrorBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Error build() {
            return new Error(status, message, reason);
        }
    }
}
