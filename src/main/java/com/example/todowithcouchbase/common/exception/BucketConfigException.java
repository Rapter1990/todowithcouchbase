package com.example.todowithcouchbase.common.exception;

import java.io.Serial;

/**
 * Custom exception class to handle invalid bucket configuration scenarios.
 * This exception is thrown when there is an issue with the bucket configuration in the system.
 * It extends {@link RuntimeException} and provides custom error messages related to bucket configuration.
 */
public class BucketConfigException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3495060754159298992L;

    private static final String DEFAULT_MESSAGE = """
            Bucket Config is not valid!
            """;

    /**
     * Default constructor that uses the default error message.
     * This constructor is used when no additional message is provided for the exception.
     */
    public BucketConfigException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructor that allows for a custom message to be provided.
     * This constructor is used when a more specific error message is needed.
     *
     * @param message The custom message that provides more details about the exception.
     */
    public BucketConfigException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}

