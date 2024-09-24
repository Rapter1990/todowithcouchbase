package com.example.todowithcouchbase.auth.model.entity;

import com.example.todowithcouchbase.common.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document
public class InvalidTokenEntity extends BaseEntity {

    @Id
    @Field(name = "ID")
    private String id;

    @Field(name = "TOKEN_ID")
    private String tokenId;

}
