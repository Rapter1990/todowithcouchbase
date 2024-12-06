package com.example.todowithcouchbase.task.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * Custom exception thrown when a task is not found in the system.
 * This exception is typically used when a task cannot be located based on the provided identifier.
 */
public class TaskNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7222351960801029610L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DEFAULT_MESSAGE = """
            Task not found!
            """;

    /**
     * Constructs a new TaskNotFoundException with the default error message.
     * This constructor is used when a task is not found, and no additional details are provided.
     */
    public TaskNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a new TaskNotFoundException with a custom error message.
     * This constructor is used when a task is not found, and an additional message is provided to further explain the context.
     *
     * @param message the custom message to be appended to the default error message.
     */
    public TaskNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
