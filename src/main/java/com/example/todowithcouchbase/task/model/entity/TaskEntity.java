package com.example.todowithcouchbase.task.model.entity;


import com.example.todowithcouchbase.common.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.Scope;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Scope("task-scope")
@Collection("task-collection")
public class TaskEntity extends BaseEntity {

    @Id
    @Field(name = "ID")
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    private String name;

}
