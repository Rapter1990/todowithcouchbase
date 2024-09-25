package com.example.todowithcouchbase.logging.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.Scope;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
@Scope("log")
@Collection("log")
public class LogEntity {

    @Id
    @Field(name = "ID")
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    private String message;

    @Field
    private String endpoint;

    @Field
    private String method;

    @Field
    private String status;

    @Field
    private String userInfo;

    @Field
    private String errorType;

    @Field
    private String response;

    @Field
    private String operation;

    @Field
    private LocalDateTime time;
}
