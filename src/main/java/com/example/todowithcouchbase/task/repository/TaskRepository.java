package com.example.todowithcouchbase.task.repository;

import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface TaskRepository extends CouchbaseRepository<TaskEntity,String> {

    boolean existsByName(String name);
}
