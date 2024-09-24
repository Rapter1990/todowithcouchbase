package com.example.todowithcouchbase.auth.exception;

import java.io.Serial;

public class PasswordNotValidException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8180169011759975985L;

    private static final String DEFAULT_MESSAGE = """
            Password is not valid!
            """;

    public PasswordNotValidException() {
        super(DEFAULT_MESSAGE);
    }

    public PasswordNotValidException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
