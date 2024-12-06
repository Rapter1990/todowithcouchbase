package com.example.todowithcouchbase.logging.entity;

import com.example.todowithcouchbase.common.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.Scope;

import java.time.LocalDateTime;

/**
 * Represents a log entity that stores information about requests and responses, including any errors that occur.
 * This class is used for logging operations such as method calls, status codes, messages, user information,
 * error details, and other relevant data. The logs are stored in a MongoDB collection for persistence and auditing purposes.
 * It extends {@link BaseEntity} to inherit common entity properties.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document
@Scope("log-scope")
@Collection("log-collection")
public class LogEntity extends BaseEntity {

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
