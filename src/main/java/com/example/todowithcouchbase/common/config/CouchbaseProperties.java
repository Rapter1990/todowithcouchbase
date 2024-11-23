package com.example.todowithcouchbase.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.couchbase")
public class CouchbaseProperties {

    private String connectionString;
    private String username;
    private String password;
    private String bucket;
    private Scopes scopes;

    @Data
    public static class Scopes {
        private String userScope;
        private String taskScope;
        private String invalidTokenScope;
        private String logScope;
    }

    private Collections collections;

    @Data
    public static class Collections {
        private String userCollection;
        private String taskCollection;
        private String invalidTokenCollection;
        private String logCollection;
    }

}
