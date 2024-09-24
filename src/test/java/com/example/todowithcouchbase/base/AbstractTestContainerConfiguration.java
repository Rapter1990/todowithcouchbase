package com.example.todowithcouchbase.base;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractTestContainerConfiguration {

    static CouchbaseContainer COUCHBASE_CONTAINER = new CouchbaseContainer("couchbase/server:7.0.3")
                .withCredentials("Administrator", "123456");

    @BeforeAll
    public static void init() {
        COUCHBASE_CONTAINER.start();
    }

    @DynamicPropertySource
    private static void overrideProps(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.couchbase.connection-string", COUCHBASE_CONTAINER::getConnectionString);
        dynamicPropertyRegistry.add("spring.couchbase.username", COUCHBASE_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.couchbase.password", COUCHBASE_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("spring.couchbase.bucket-name", () -> "testbucket");
    }
}
