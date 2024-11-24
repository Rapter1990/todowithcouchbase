package com.example.todowithcouchbase.task.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class TaskNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7222351960801029610L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DEFAULT_MESSAGE = """
            Task not found!
            """;

    public TaskNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public TaskNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
