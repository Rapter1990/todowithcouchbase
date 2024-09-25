package com.example.todowithcouchbase.auth.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class RoleNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4353606558549954783L;

    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String DEFAULT_MESSAGE = """
            Role not found!
            """;

    public RoleNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public RoleNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
