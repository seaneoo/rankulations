package com.seaneoo.rankulations.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MismatchedPasswordsException extends ResponseStatusException {

    public MismatchedPasswordsException() {
        super(HttpStatus.BAD_REQUEST, "Passwords do not match.");
    }
}
