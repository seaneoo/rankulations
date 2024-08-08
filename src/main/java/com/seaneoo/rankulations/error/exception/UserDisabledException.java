package com.seaneoo.rankulations.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserDisabledException extends ResponseStatusException {

    public UserDisabledException() {
        super(HttpStatus.UNAUTHORIZED, "User is disabled.");
    }
}
