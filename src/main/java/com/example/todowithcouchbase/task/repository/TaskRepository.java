package com.example.todowithcouchbase.task.repository;


import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link TaskEntity} entities in the Couchbase database.
 * This interface extends {@link CouchbaseRepository}, providing CRUD operations for {@link TaskEntity}.
 * It also defines custom query methods for working with tasks by name.
 */
public interface TaskRepository extends CouchbaseRepository<TaskEntity,String> {

    /**
     * Checks if a task with the specified name already exists in the database.
     *
     * @param name the name of the task to check for existence.
     * @return {@code true} if a task with the specified name exists, {@code false} otherwise.
     */
    boolean existsByName(String name);

    /**
     * Finds a task entity by its name.
     *
     * @param name the name of the task to find.
     * @return an {@link Optional} containing the {@link TaskEntity} if found, or an empty {@link Optional} if not.
     */
    Optional<TaskEntity> findTaskByName(String name);

}
