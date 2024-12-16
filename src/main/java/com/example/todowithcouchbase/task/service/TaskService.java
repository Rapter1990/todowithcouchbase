package com.example.todowithcouchbase.task.service;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.request.CustomPagingRequest;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.GetTaskByNameRequest;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;

/**
 * Service interface for managing tasks in the system.
 * This interface defines the methods for CRUD operations on tasks,
 * including saving, retrieving, updating, and deleting tasks.
 */
public interface TaskService {

    /**
     * Saves a new task to the database.
     *
     * @param taskRequest the request object containing the details of the task to be saved.
     * @return the saved {@link Task} entity.
     */
    Task saveTask(SaveTaskRequest taskRequest);

    /**
     * Retrieves all tasks with pagination support.
     *
     * @param customPagingRequest the request object containing paging parameters (e.g., page size, page number).
     * @return a {@link CustomPage} of tasks, containing a list of tasks and pagination details.
     */
    CustomPage<Task> getAllTasks(final CustomPagingRequest customPagingRequest);

    /**
     * Retrieves a task by its name.
     *
     * @param getTaskByNameRequest the request object containing the name of the task to be retrieved.
     * @return the {@link Task} entity with the specified name.
     */
    Task getTaskByName(final GetTaskByNameRequest getTaskByNameRequest);

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to be retrieved.
     * @return the {@link Task} entity with the specified ID.
     */
    Task getTaskById(final String id);

    /**
     * Updates an existing task by its ID.
     *
     * @param id the ID of the task to be updated.
     * @param updateTaskRequest the request object containing the updated details of the task.
     * @return the updated {@link Task} entity.
     */
    Task updateTaskById(final String id, final UpdateTaskRequest updateTaskRequest);

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to be deleted.
     */
    void deleteTaskById(String id);

}
