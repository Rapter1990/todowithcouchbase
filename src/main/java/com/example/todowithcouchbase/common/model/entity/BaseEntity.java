package com.example.todowithcouchbase.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.couchbase.core.mapping.Field;

import java.time.LocalDateTime;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {


    @Field(name = "createdAt")
    private LocalDateTime createdAt;

    @Field(name = "createdBy")
    private String createdBy;

    @Field(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Field(name = "updatedBy")
    private String updatedBy;

}
