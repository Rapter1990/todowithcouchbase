package com.example.todowithcouchbase.Task.repository;

import com.example.todowithcouchbase.Task.model.entity.TaskEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CouchbaseRepository<TaskEntity,String> {

    @Query("SELECT COUNT(*) AS count FROM `todo_list` WHERE name = $1")
    boolean existsTaskEntitiesByName(String name);
}
