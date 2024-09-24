package com.example.todowithcouchbase.auth.exception;

import java.io.Serial;

public class UserAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6595921257346957788L;

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
