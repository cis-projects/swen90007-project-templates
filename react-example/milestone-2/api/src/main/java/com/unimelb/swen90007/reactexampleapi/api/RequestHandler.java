package com.unimelb.swen90007.reactexampleapi.api;

@FunctionalInterface
interface RequestHandler {
    ResponseEntity handle();
}
