package com.seaneoo.rankulations.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameTakenException extends ResponseStatusException {

    public UsernameTakenException() {
        super(HttpStatus.CONFLICT, "Username is already taken.");
    }
}
