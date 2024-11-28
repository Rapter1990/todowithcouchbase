package com.example.todowithcouchbase.common.config;

import com.couchbase.client.core.env.IoConfig;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.manager.bucket.BucketManager;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;
import com.example.todowithcouchbase.common.exception.BucketConfigException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

import java.time.Duration;

@Slf4j
@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    private final CouchbaseProperties couchbaseProperties;

    public CouchbaseConfig(CouchbaseProperties couchbaseProperties) {
        this.couchbaseProperties = couchbaseProperties;
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

    @Bean
    public ClusterEnvironment couchbaseClusterEnvironment() {
        return ClusterEnvironment.builder()
                .ioConfig(IoConfig.enableDnsSrv(false))
                .build();
    }

    @Bean
    public Cluster couchbaseCluster() {
        ClusterEnvironment environment = couchbaseClusterEnvironment();
        Cluster cluster = Cluster.connect(getConnectionString(), ClusterOptions
                .clusterOptions(getUserName(), getPassword())
                .environment(environment));

        // Wait until the cluster is ready
        cluster.waitUntilReady(Duration.ofSeconds(10));
        return cluster;
    }

    @Bean
    public Bucket couchbaseBucket() {
        Cluster cluster = couchbaseCluster();
        BucketManager bucketManager = cluster.buckets();

        if (!bucketManager.getAllBuckets().containsKey(getBucketName())) {
            BucketSettings bucketSettings = BucketSettings.create(getBucketName())
                    .bucketType(BucketType.COUCHBASE)
                    .ramQuotaMB(100)
                    .flushEnabled(true);

            log.info("Creating Couchbase bucket: {}", getBucketName());
            bucketManager.createBucket(bucketSettings);

            Bucket bucket = cluster.bucket(getBucketName());
            bucket.waitUntilReady(Duration.ofSeconds(10));
            createScopeAndCollection(bucket, couchbaseProperties.getScopes().getUserScope(), couchbaseProperties.getCollections().getUserCollection());
            createScopeAndCollection(bucket, couchbaseProperties.getScopes().getTaskScope(), couchbaseProperties.getCollections().getTaskCollection());
            createScopeAndCollection(bucket, couchbaseProperties.getScopes().getInvalidTokenScope(), couchbaseProperties.getCollections().getInvalidTokenCollection());
            createScopeAndCollection(bucket, couchbaseProperties.getScopes().getLogScope(), couchbaseProperties.getCollections().getLogCollection());
            return bucket;
        } else {
            log.info("Couchbase bucket '{}' already exists", getBucketName());
        }
        return cluster.bucket(getBucketName());
    }

    private void createScopeAndCollection(Bucket bucket, String scopeName, String collectionName) {
        try {
            Scope scope = bucket.scope(scopeName);
            if (scope == null) {
                log.info("Scope {} does not exist. Creating it now", scopeName);
                bucket.collections().createScope(scopeName);
                bucket.collections().createCollection(scopeName, collectionName);
            }
        } catch (Exception e) {
            log.error("Error creating scope or collection. Scope: {}, Collection: {}", scopeName, collectionName, e);
            throw new BucketConfigException();
        }
    }

    @Bean
    public Scope userScope() {
        return couchbaseBucket().scope(couchbaseProperties.getScopes().getUserScope());
    }

    @Bean
    public Scope taskScope() {
        return couchbaseBucket().scope(couchbaseProperties.getScopes().getTaskScope());
    }

    @Bean
    public Scope invalidTokenScope() {
        return couchbaseBucket().scope(couchbaseProperties.getScopes().getInvalidTokenScope());
    }

    @Bean
    public Scope logScope() {
        return couchbaseBucket().scope(couchbaseProperties.getScopes().getLogScope());
    }

}

