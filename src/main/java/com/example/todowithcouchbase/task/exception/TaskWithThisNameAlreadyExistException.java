package com.example.todowithcouchbase.task.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class TaskWithThisNameAlreadyExistException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -3495060754159297663L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DEFAULT_MESSAGE = """
            Task with this name already exist
            """;

    public TaskWithThisNameAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public TaskWithThisNameAlreadyExistException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }
}
