package com.example.todowithcouchbase.common.exception;

import java.io.Serial;

public class BucketConfigException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3495060754159298992L;

    private static final String DEFAULT_MESSAGE = """
            Bucket Config is not valid!
            """;

    public BucketConfigException() {
        super(DEFAULT_MESSAGE);
    }

    public BucketConfigException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}

