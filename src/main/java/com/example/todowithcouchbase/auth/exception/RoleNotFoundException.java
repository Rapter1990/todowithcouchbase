package com.example.todowithcouchbase.auth.exception;

import java.io.Serial;

public class RoleNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4353606558549954783L;

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
