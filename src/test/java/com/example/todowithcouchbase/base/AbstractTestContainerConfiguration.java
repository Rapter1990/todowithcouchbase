package com.example.todowithcouchbase.base;

import com.couchbase.client.core.error.CollectionExistsException;
import com.couchbase.client.core.error.CollectionNotFoundException;
import com.couchbase.client.core.error.ScopeExistsException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.collection.CollectionManager;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.data.couchbase.core.CouchbaseQueryExecutionException;
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
        createBucketIfNotExists(cluster);

        // Access the bucket and ensure it's ready
        Bucket bucket = cluster.bucket(BUCKET_NAME);
        bucket.waitUntilReady(Duration.ofSeconds(10));
        CollectionManager collectionManager = bucket.collections();

        // Create scopes and collections (according to your configuration)
        createScopeAndCollection(collectionManager, "invalid-token-scope", "invalid-token-collection");
        createScopeAndCollection(collectionManager, "user-scope", "user-collection");
        createScopeAndCollection(collectionManager, "task-scope", "task-collection");
        createScopeAndCollection(collectionManager, "log-scope", "log-collection");

        // Ensure all necessary collections exist
        ensureCollectionExists(bucket, "invalid-token-scope", "invalid-token-collection");
        ensureCollectionExists(bucket, "user-scope", "user-collection");
        ensureCollectionExists(bucket, "task-scope", "task-collection");
        ensureCollectionExists(bucket, "log-scope", "log-collection");

        // Ensure primary indexes are created on all collections (across scopes)
        createPrimaryIndexIfNotExists(cluster, bucket, "invalid-token-scope", "invalid-token-collection");
        createPrimaryIndexIfNotExists(cluster, bucket, "user-scope", "user-collection");
        createPrimaryIndexIfNotExists(cluster, bucket, "task-scope", "task-collection");
        createPrimaryIndexIfNotExists(cluster, bucket, "log-scope", "log-collection");


        cluster.disconnect();
    }

    private static void createBucketIfNotExists(Cluster cluster) {
        try {
            // Check if the bucket already exists
            cluster.buckets().getBucket(BUCKET_NAME);
        } catch (Exception e) {
            log.info("Bucket '{}' not found. Creating a new bucket.", BUCKET_NAME);
            cluster.buckets().createBucket(
                    BucketSettings.create(BUCKET_NAME)
                            .flushEnabled(true)
                            .ramQuotaMB(100)
                            .numReplicas(1)
            );
        }
    }

    private static void createScopeAndCollection(CollectionManager manager, String scopeName, String collectionName) {
        try {
            // Create the scope if it does not exist
            try {
                manager.createScope(scopeName);
                log.info("Scope created: {}", scopeName);
            } catch (ScopeExistsException ignored) {
                log.warn("Scope already exists: {}", scopeName);
            }

            // Create the collection under the given scope if it does not exist
            try {
                manager.createCollection(scopeName, collectionName);
                log.info("Collection created: {}.{}", scopeName, collectionName);
            } catch (CollectionExistsException ignored) {
                log.warn("Collection already exists in scope {}: {}", scopeName, collectionName);
            }
        } catch (Exception e) {
            log.error("Error creating scope or collection: {}.{}", scopeName, collectionName, e);
        }
    }

    private static void ensureCollectionExists(Bucket bucket, String scopeName, String collectionName) {
        try {
            Collection collection = bucket.scope(scopeName).collection(collectionName);
            log.info("Collection exists: {}.{}", scopeName, collectionName);
        } catch (CollectionNotFoundException e) {
            log.info("Collection not found: {}.{} - Creating it.", scopeName, collectionName);
            bucket.collections().createCollection(scopeName, collectionName);
        }
    }

    private static void createPrimaryIndexIfNotExists(Cluster cluster, Bucket bucket, String scopeName, String collectionName) {
        try {
            // Query for a primary index (using the collection context)
            String indexQuery = String.format(
                    "SELECT * FROM %s.%s.%s LIMIT 1",
                    bucket.name(),
                    scopeName,
                    collectionName
            );

            try {
                // Execute the query to check if the primary index exists
                QueryResult result = cluster.query(
                        indexQuery,
                        QueryOptions.queryOptions().adhoc(false) // Makes the query non-adhoc (optimizing)
                );
                log.info("Primary index exists or query was successful.");
            } catch (CouchbaseQueryExecutionException e) {
                log.warn("Primary index not found on collection {}.{}. Creating it.", scopeName, collectionName);
                // Create the primary index if it does not exist
                String createIndexQuery = String.format(
                        "CREATE PRIMARY INDEX ON %s.%s.%s USING GSI",
                        bucket.name(),
                        scopeName,
                        collectionName
                );
                cluster.query(createIndexQuery);
                log.info("Primary index created on collection {}.{}", scopeName, collectionName);
            }
        } catch (Exception e) {
            log.error("Error creating primary index for collection {}.{}", scopeName, collectionName, e);
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
