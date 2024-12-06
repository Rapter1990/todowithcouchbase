package com.example.todowithcouchbase.auth.model.entity;

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

/**
 * Represents an entity that stores information about invalidated tokens.
 * This class is used to store a record of tokens that have been invalidated
 * in a MongoDB collection. Each invalid token is identified by a unique token ID.
 * This entity extends from {@link BaseEntity}, inheriting common fields such as
 * created and updated timestamps.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document
@Scope("invalid-token-scope")
@Collection("invalid-token-collection")
public class InvalidTokenEntity extends BaseEntity {

    @Id
    @Field(name = "ID")
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field(name = "TOKEN_ID")
    private String tokenId;

}
