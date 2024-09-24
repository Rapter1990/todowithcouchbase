package com.example.todowithcouchbase.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenApiConfigTest {

    @Test
    void openApiInfo() {

        // Given
        OpenAPIDefinition openAPIDefinition = OpenApiConfig.class.getAnnotation(OpenAPIDefinition.class);

        // Then
        assertEquals("1.0.0", openAPIDefinition.info().version());
        assertEquals("todowithcouchbase", openAPIDefinition.info().title());
        assertEquals("Case Study - To Do List with Spring Boot and Couchbase" +
                        "(Case Study - To Do List Example (Java 21, Spring Boot, Couchbase, JUnit, Spring Security, JWT, Docker, Kubernetes, Prometheus, Grafana, Github Actions (CI/CD), SonarQube) ",
                openAPIDefinition.info().description());

    }

    @Test
    void securityScheme() {

        // Given
        SecurityScheme securityScheme = OpenApiConfig.class.getAnnotation(SecurityScheme.class);

        // Then
        assertEquals("bearerAuth", securityScheme.name());
        assertEquals("JWT Token", securityScheme.description());
        assertEquals("bearer", securityScheme.scheme());
        assertEquals(SecuritySchemeType.HTTP, securityScheme.type());
        assertEquals("JWT", securityScheme.bearerFormat());
        assertEquals(SecuritySchemeIn.HEADER, securityScheme.in());

    }

    @Test
    void contactInfo() {

        // Given
        Info info = OpenApiConfig.class.getAnnotation(OpenAPIDefinition.class).info();
        Contact contact = info.contact();

        // Then
        assertEquals("Sercan Noyan Germiyanoğlu | Süha Can Uluer | Mehmet Şeymus Yüzen | Harun Yusuf Ekşioğlu | Muhammet Oğuzhan Aydoğan", contact.name());
        assertEquals("https://github.com/Rapter1990/todowithcouchbase", contact.url());

    }

}