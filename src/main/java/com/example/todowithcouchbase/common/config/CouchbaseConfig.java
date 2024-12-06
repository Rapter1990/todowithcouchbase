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

/**
 * Configuration class for setting up the Couchbase connection and bucket management.
 * This class handles the configuration of the Couchbase cluster, including connecting to the cluster,
 * setting up the bucket, and creating scopes and collections. It also provides beans for interacting
 * with Couchbase through scopes and collections. The configuration is customized through the
 * {@link CouchbaseProperties} class.
 *
 */
@Slf4j
@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    private final CouchbaseProperties couchbaseProperties;

    /**
     * Constructor to initialize the {@link CouchbaseConfig} with the provided {@link CouchbaseProperties}.
     *
     * @param couchbaseProperties The {@link CouchbaseProperties} containing connection details for Couchbase.
     */
    public CouchbaseConfig(CouchbaseProperties couchbaseProperties) {
        this.couchbaseProperties = couchbaseProperties;
    }

    /**
     * Returns the connection string for Couchbase.
     *
     * @return The connection string to connect to Couchbase.
     */
    @Override
    public String getConnectionString() {
        return couchbaseProperties.getConnectionString();
    }

    /**
     * Returns the username for authenticating to the Couchbase cluster.
     *
     * @return The username for the Couchbase cluster.
     */
    @Override
    public String getUserName() {
        return couchbaseProperties.getUsername();
    }

    /**
     * Returns the password for authenticating to the Couchbase cluster.
     *
     * @return The password for the Couchbase cluster.
     */
    @Override
    public String getPassword() {
        return couchbaseProperties.getPassword();
    }

    /**
     * Returns the bucket name for the Couchbase database.
     *
     * @return The name of the Couchbase bucket.
     */
    @Override
    public String getBucketName() {
        return couchbaseProperties.getBucket();
    }

    /**
     * Creates and configures the {@link ClusterEnvironment} bean for connecting to the Couchbase cluster.
     * Disables DNS SRV (Service Record) lookups for connecting to the Couchbase cluster.
     *
     * @return The {@link ClusterEnvironment} configured for the Couchbase connection.
     */
    @Bean
    public ClusterEnvironment couchbaseClusterEnvironment() {
        return ClusterEnvironment.builder()
                .ioConfig(IoConfig.enableDnsSrv(false))
                .build();
    }

    /**
     * Creates and configures the {@link Cluster} bean to connect to the Couchbase cluster.
     * The method waits until the cluster is ready before returning the cluster instance.
     *
     * @return The {@link Cluster} instance for interacting with Couchbase.
     */
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

    /**
     * Creates and configures the {@link Bucket} bean for the specified Couchbase bucket.
     * If the bucket does not exist, it creates the bucket and the necessary scopes and collections.
     *
     * @return The {@link Bucket} instance connected to the specified Couchbase bucket.
     */
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

    /**
     * Creates the specified scope and collection in the given {@link Bucket}.
     * If the scope does not exist, it will be created. Similarly, the collection will be created if it does not exist.
     *
     * @param bucket The {@link Bucket} where the scope and collection will be created.
     * @param scopeName The name of the scope to be created.
     * @param collectionName The name of the collection to be created.
     */
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

    /**
     * Provides a {@link Scope} bean for the user scope in the Couchbase bucket.
     *
     * @return The {@link Scope} for the user scope.
     */
    @Bean
    public Scope userScope() {
        return couchbaseBucket().scope(couchbaseProperties.getScopes().getUserScope());
    }

    /**
     * Provides a {@link Scope} bean for the task scope in the Couchbase bucket.
     *
     * @return The {@link Scope} for the task scope.
     */
    @Bean
    public Scope taskScope() {
        return couchbaseBucket().scope(couchbaseProperties.getScopes().getTaskScope());
    }

    /**
     * Provides a {@link Scope} bean for the invalid token scope in the Couchbase bucket.
     *
     * @return The {@link Scope} for the invalid token scope.
     */
    @Bean
    public Scope invalidTokenScope() {
        return couchbaseBucket().scope(couchbaseProperties.getScopes().getInvalidTokenScope());
    }

    /**
     * Provides a {@link Scope} bean for the log scope in the Couchbase bucket.
     *
     * @return The {@link Scope} for the log scope.
     */
    @Bean
    public Scope logScope() {
        return couchbaseBucket().scope(couchbaseProperties.getScopes().getLogScope());
    }

}

