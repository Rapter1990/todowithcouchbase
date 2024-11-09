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
