package com.example.todowithcouchbase.task.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * Custom exception thrown when a task with the same name already exists in the system.
 * This exception is typically used when attempting to create or update a task with a name
 * that already exists, violating the uniqueness constraint.
 */
public class TaskWithThisNameAlreadyExistException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -3495060754159297663L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DEFAULT_MESSAGE = """
            Task with this name already exist
            """;

    /**
     * Constructs a new TaskWithThisNameAlreadyExistException with the default error message.
     * This constructor is used when a task with the same name already exists, and no additional details are provided.
     */
    public TaskWithThisNameAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a new TaskWithThisNameAlreadyExistException with a custom error message.
     * This constructor is used when a task with the same name already exists, and an additional message is provided
     * to further explain the context.
     *
     * @param message the custom message to be appended to the default error message.
     */
    public TaskWithThisNameAlreadyExistException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
