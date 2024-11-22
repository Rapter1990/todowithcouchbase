package com.example.todowithcouchbase.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "spring.couchbase")
public class CouchbaseProperties {

    private String connectionString;
    private String username;
    private String password;
    private String bucket;
    private boolean enableTls;
    private boolean enableDnsSrv;
    private boolean autoIndexCreation;

    private Map<String, String> scopes;
    private Map<String, String> collections;

}