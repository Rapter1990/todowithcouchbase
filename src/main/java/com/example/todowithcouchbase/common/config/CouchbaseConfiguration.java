package com.example.todowithcouchbase.common.config;

import com.couchbase.client.java.env.ClusterEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.auditing.EnableCouchbaseAuditing;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;


@Configuration
@EnableCouchbaseRepositories
@EnableCouchbaseAuditing
@RequiredArgsConstructor
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {
    private final CouchbaseProperties couchbaseProperties;

    @Override
    protected void configureEnvironment(final ClusterEnvironment.Builder builder) {
        builder.securityConfig().enableTls(couchbaseProperties.isEnableTls());
        builder.ioConfig().enableDnsSrv(couchbaseProperties.isEnableDnsSrv());
    }

    @Override
    public String getConnectionString() {
        return couchbaseProperties.getConnectionString();
    }

    @Override
    public String getUserName() {
        return couchbaseProperties.getUsername();
    }

    @Override
    public String getPassword() {
        return couchbaseProperties.getPassword();
    }

    @Override
    public String getBucketName() {
        return couchbaseProperties.getBucket();
    }

    //still not working with spring data, so we can delete this method
    @Override
    protected boolean autoIndexCreation() { return couchbaseProperties.isAutoIndexCreation(); }
}