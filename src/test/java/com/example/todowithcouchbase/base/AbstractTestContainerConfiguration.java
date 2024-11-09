package com.example.todowithcouchbase.base;

import com.couchbase.client.core.error.CollectionExistsException;
import com.couchbase.client.core.error.ScopeExistsException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.collection.CollectionManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@Slf4j
@Testcontainers
public abstract class AbstractTestContainerConfiguration {

    private static final String BUCKET_NAME = "todo_list";

    static CouchbaseContainer COUCHBASE_CONTAINER = new CouchbaseContainer("couchbase/server:7.0.3")
            .withCredentials("Administrator", "123456");

    @BeforeAll
    public static void setup() {
        COUCHBASE_CONTAINER.start();

        // Connect to Couchbase cluster
        Cluster cluster = Cluster.connect(
                COUCHBASE_CONTAINER.getConnectionString(),
                COUCHBASE_CONTAINER.getUsername(),
                COUCHBASE_CONTAINER.getPassword()
        );

        // Create the bucket if it doesn't exist
        createBucketIfNotExists(cluster, BUCKET_NAME);

        // Access the bucket and ensure it's ready
        Bucket bucket = cluster.bucket(BUCKET_NAME);
        bucket.waitUntilReady(Duration.ofSeconds(10));
        CollectionManager collectionManager = bucket.collections();

        // Create scopes and collections (according to your configuration)
        createScopeAndCollection(collectionManager, "invalid-token", "invalid-token");
        createScopeAndCollection(collectionManager, "user", "user");
        createScopeAndCollection(collectionManager, "task", "task");
        createScopeAndCollection(collectionManager, "log", "log");

        // Ensure invalid-token collection exists within the invalid-token-scope


        cluster.disconnect();
    }

    private static void createBucketIfNotExists(Cluster cluster, String bucketName) {
        try {
            // Check if the bucket already exists
            cluster.buckets().getBucket(bucketName);
        } catch (Exception e) {
            log.info("Bucket '{}' not found. Creating a new bucket.", bucketName);
            cluster.buckets().createBucket(
                    BucketSettings.create(bucketName)
                            .flushEnabled(true)
                            .ramQuotaMB(100)
                            .numReplicas(1)
            );
        }
    }

    private static void createScopeAndCollection(CollectionManager manager, String scopeName, String collectionName) {
        try {
            // Create the scope if it does not exist
            manager.createScope(scopeName);
            log.info("Scope created: {}", scopeName);
        } catch (ScopeExistsException ignored) {
            log.warn("Scope already exists: {}", scopeName);
        }

        try {
            // Create the collection under the given scope if it does not exist
            manager.createCollection(scopeName, collectionName);
            log.info("Collection created: {}.{}", scopeName, collectionName);
        } catch (CollectionExistsException ignored) {
            log.warn("Collection already exists in scope {}: {}", scopeName, collectionName);
        }
    }

    @DynamicPropertySource
    private static void overrideProps(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.couchbase.connection-string", COUCHBASE_CONTAINER::getConnectionString);
        dynamicPropertyRegistry.add("spring.couchbase.username", COUCHBASE_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.couchbase.password", COUCHBASE_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("spring.couchbase.bucket", () -> BUCKET_NAME);
    }
}

