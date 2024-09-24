package com.example.todowithcouchbase.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Sercan Noyan Germiyanoğlu | Süha Can Uluer | Mehmet Şeymus Yüzen | Harun Yusuf Ekşioğlu | Muhammet Oğuzhan Aydoğan",
                        url = "https://github.com/Rapter1990/todowithcouchbase"
                ),
                description = "Case Study - To Do List with Spring Boot and Couchbase" +
                        "(Case Study - To Do List Example (Java 21, Spring Boot, Couchbase, JUnit, Spring Security, JWT, Docker, Kubernetes, Prometheus, Grafana, Github Actions (CI/CD), SonarQube) ",
                title = "todowithcouchbase",
                version = "1.0.0"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
