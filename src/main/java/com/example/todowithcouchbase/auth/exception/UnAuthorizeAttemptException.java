package com.example.todowithcouchbase.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;


public class UnAuthorizeAttemptException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 668466158918970622L;

    public static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    private static final String DEFAULT_MESSAGE = """
            You do not have permission to create a to-do item.
            """;

    public UnAuthorizeAttemptException() {
        super(DEFAULT_MESSAGE);
    }

}
