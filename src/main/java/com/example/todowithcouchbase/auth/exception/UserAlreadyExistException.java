package com.example.todowithcouchbase.auth.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class UserAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6595921257346957788L;

    public static final HttpStatus STATUS = HttpStatus.CONFLICT;

    private static final String DEFAULT_MESSAGE = """
            User already exist!
            """;

    public UserAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public UserAlreadyExistException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
