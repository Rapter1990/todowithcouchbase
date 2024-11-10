package com.example.todowithcouchbase.task.model;


import com.example.todowithcouchbase.common.model.BaseDomainModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
