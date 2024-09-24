package com.example.todowithcouchbase.common.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.manager.bucket.BucketManager;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;
import com.example.todowithcouchbase.common.exception.BucketConfigException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

@Slf4j
@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${spring.couchbase.connection-string}")
    private String connectionString;

    @Value("${spring.couchbase.username}")
    private String username;

    @Value("${spring.couchbase.password}")
    private String password;

    @Value("${spring.couchbase.bucket}")
    private String bucketName;

    @Value("${spring.couchbase.scopes.user-scope}")
    private String userScope;

    @Value("${spring.couchbase.scopes.task-scope}")
    private String taskScope;

    @Value("${spring.couchbase.scopes.invalid-token-scope}")
    private String invalidTokenScope;

    @Value("${spring.couchbase.collections.user-scope}")
    private String userCollections;

    @Value("${spring.couchbase.collections.task-scope}")
    private String taskCollections;

    @Value("${spring.couchbase.collections.invalid-token-scope}")
    private String invalidTokenCollections;

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Bean
    public Cluster couchbaseCluster() {
        return Cluster.connect(connectionString, username, password);
    }

    @Bean
    public Bucket couchbaseBucket() {
        Cluster cluster = couchbaseCluster();

        // Check if bucket exists, if not, create it
        BucketManager bucketManager = cluster.buckets();
        if (!bucketManager.getAllBuckets().containsKey(bucketName)) {
            BucketSettings bucketSettings = BucketSettings.create(bucketName)
                    .bucketType(BucketType.COUCHBASE)  // Set the bucket type (can be COUCHBASE or EPHEMERAL)
                    .ramQuotaMB(100)                   // Set RAM quota in MB
                    .flushEnabled(true);               // Enable flush if needed

            log.info("Creating Couchbase bucket: {}", bucketName);

            bucketManager.createBucket(bucketSettings);

            var bucket = cluster.bucket(bucketName);
            createScopeAndCollection(bucket, userScope, userCollections);
            createScopeAndCollection(bucket, taskScope, taskCollections);
            createScopeAndCollection(bucket, invalidTokenScope, invalidTokenCollections);

        } else {
            log.info("Couchbase bucket '{}' already exists", bucketName);
        }

        return cluster.bucket(bucketName);
    }

    private void createScopeAndCollection(Bucket bucket, String scopName, String collectionName) {
        try {
            // Check if the scope exists
            Scope userScope = bucket.scope(scopName);
            if (userScope != null) {
                log.info("Scope {} does not exist. Creating it now", scopName);
                bucket.collections().createScope(scopName);
                bucket.collections().createCollection(scopName, collectionName);
            }

        } catch (Exception e) {
            log.error("Error creating scope or collection. Scope: {}, Collection: {}", scopName, collectionName, e);
            throw new BucketConfigException();
        }
    }

    @Bean
    public Scope userScope() {
        return couchbaseBucket().scope(userScope);
    }

    @Bean
    public Scope taskScope() {
        return couchbaseBucket().scope(taskScope);
    }

    @Bean
    public Scope invalidTokenScope() {
        return couchbaseBucket().scope(invalidTokenScope);
    }

}

