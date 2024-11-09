package com.example.todowithcouchbase.task.repository;

import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CouchbaseRepository<TaskEntity,String> {

    @Query("SELECT COUNT(*) > 0 AS exists FROM `todo_list` WHERE name = $name")
    boolean existsTaskEntitiesByName(@Param("name") String name);
}
