package com.example.todowithcouchbase.task.repository;


import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;


public interface TaskRepository extends CouchbaseRepository<TaskEntity,String> {

    boolean existsByName(String name);

    Optional<TaskEntity> findTaskByName(String name);

}
