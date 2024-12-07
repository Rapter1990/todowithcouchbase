package com.example.todowithcouchbase.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class holds the Couchbase configuration properties.
 * It is used to load properties from the application's configuration (typically application.yml or application.properties)
 * and provides them in a structured format to be used for Couchbase connectivity and configuration.
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.couchbase")
public class CouchbaseProperties {

    private String connectionString;
    private String username;
    private String password;
    private String bucket;
    private Scopes scopes;

    /**
     * Nested class that holds the scope names used within Couchbase.
     * Each scope contains multiple collections that are logically grouped together.
     */
    @Data
    public static class Scopes {
        private String userScope;
        private String taskScope;
        private String invalidTokenScope;
        private String logScope;
    }

    private Collections collections;

    /**
     * Nested class that holds the collection names used within Couchbase.
     * Each collection belongs to a specific scope.
     */
    @Data
    public static class Collections {
        private String userCollection;
        private String taskCollection;
        private String invalidTokenCollection;
        private String logCollection;
    }

}
