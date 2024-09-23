package com.example.todowithcouchbase.common.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.manager.bucket.BucketManager;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;
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
        } else {
            log.info("Couchbase bucket '{}' already exists", bucketName);
        }

        return cluster.bucket(bucketName);
    }

    @Bean
    public Scope userScope() {
        return couchbaseBucket().scope(userScope);
    }

    @Bean
    public Scope taskScope() {
        return couchbaseBucket().scope(taskScope);
    }
}

