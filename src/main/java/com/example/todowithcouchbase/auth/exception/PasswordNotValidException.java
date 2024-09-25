package com.example.todowithcouchbase.auth.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class PasswordNotValidException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8180169011759975985L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

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
