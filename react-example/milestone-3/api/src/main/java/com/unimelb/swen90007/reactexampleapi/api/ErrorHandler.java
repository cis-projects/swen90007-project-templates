package com.unimelb.swen90007.reactexampleapi.api;

import com.unimelb.swen90007.reactexampleapi.domain.ForbiddenException;
import com.unimelb.swen90007.reactexampleapi.domain.NotFoundException;
import com.unimelb.swen90007.reactexampleapi.domain.ValidationException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ErrorHandler implements RequestHandler {

    private final RequestHandler handler;

    public ErrorHandler(RequestHandler handler) {
        this.handler = handler;
    }

    @Override
    public ResponseEntity handle() {
        try {
            return handler.handle();
        } catch (NotFoundException e) {
            return ResponseEntity.of(HttpServletResponse.SC_NOT_FOUND,
                    Error.builder()
                            .status(HttpServletResponse.SC_NOT_FOUND)
                            .message("Not Found")
                            .reason(e.getMessage())
                            .build()
            );
        } catch (ValidationException e) {
            return ResponseEntity.of(HttpServletResponse.SC_BAD_REQUEST,
                    Error.builder()
                            .status(HttpServletResponse.SC_BAD_REQUEST)
                            .message("Bad Request")
                            .reason(e.getMessage())
                            .build()
            );
        } catch (ForbiddenException e) {
            return ResponseEntity.of(HttpServletResponse.SC_FORBIDDEN,
                    Error.builder()
                            .status(HttpServletResponse.SC_FORBIDDEN)
                            .message("Forbidden")
                            .reason(e.getMessage())
                            .build()
            );
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.of(HttpServletResponse.SC_FORBIDDEN,
                    Error.builder()
                            .status(HttpServletResponse.SC_FORBIDDEN)
                            .message("Forbidden")
                            .reason("invalid username and password combination")
                            .build()
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.of(HttpServletResponse.SC_UNAUTHORIZED,
                    Error.builder()
                            .status(HttpServletResponse.SC_UNAUTHORIZED)
                            .message("Unauthorized")
                            .reason(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.of(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    Error.builder()
                            .status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                            .message("Internal Server Error")
                            .reason(e.getMessage())
                            .build()
            );
        }

    }

    static ErrorHandler of(RequestHandler handler) {
        return new ErrorHandler(handler);
    }
}
