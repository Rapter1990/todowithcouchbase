package com.example.todowithcouchbase.Task.model;


import com.example.todowithcouchbase.common.model.BaseDomainModel;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Task extends BaseDomainModel {

    private String id;

    private String name;
}
