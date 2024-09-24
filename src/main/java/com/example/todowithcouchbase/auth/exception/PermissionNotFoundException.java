package com.example.todowithcouchbase.auth.exception;

import java.io.Serial;

public class PermissionNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7529558108657769335L;

    private static final String DEFAULT_MESSAGE = """
            Permission not found!
            """;

    public PermissionNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public PermissionNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
