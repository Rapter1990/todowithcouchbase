package com.example.todowithcouchbase.task.service.impl;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.request.CustomPagingRequest;
import com.example.todowithcouchbase.task.exception.TaskNotFoundException;
import com.example.todowithcouchbase.task.exception.TaskWithThisNameAlreadyExistException;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.GetTaskByNameRequest;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import com.example.todowithcouchbase.task.model.mapper.ListTaskEntityToListTaskMapper;
import com.example.todowithcouchbase.task.model.mapper.SaveTaskRequestToTaskEntityMapper;
import com.example.todowithcouchbase.task.model.mapper.TaskEntityToTaskMapper;
import com.example.todowithcouchbase.task.model.mapper.UpdateTaskRequestToTaskEntityMapper;
import com.example.todowithcouchbase.task.repository.TaskRepository;
import com.example.todowithcouchbase.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing tasks in the system.
 * This interface defines the methods for CRUD operations on tasks,
 * including saving, retrieving, updating, and deleting tasks.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final SaveTaskRequestToTaskEntityMapper saveTaskRequestToTaskEntityMapper =
            SaveTaskRequestToTaskEntityMapper.initialize();

    private final TaskEntityToTaskMapper taskEntityToTaskMapper =
            TaskEntityToTaskMapper.initialize();

    private final ListTaskEntityToListTaskMapper listTaskEntityToListTaskMapper =
            ListTaskEntityToListTaskMapper.initialize();

    private final UpdateTaskRequestToTaskEntityMapper updateTaskRequestToTaskEntityMapper =
            UpdateTaskRequestToTaskEntityMapper.initialize();

    /**
     * Saves a new task to the database.
     *
     * @param taskRequest the request object containing the details of the task to be saved.
     * @return the saved {@link Task} entity.
     */
    @Override
    public Task saveTaskToDatabase(final SaveTaskRequest taskRequest) {

        checkTaskNameUniqueness(taskRequest.getName());

        TaskEntity taskEntityToBeSaved=saveTaskRequestToTaskEntityMapper.mapForSaving(taskRequest);
        taskRepository.save(taskEntityToBeSaved);

        return taskEntityToTaskMapper.map(taskEntityToBeSaved);

    }

    /**
     * Retrieves all tasks with pagination support.
     *
     * @param customPagingRequest the request object containing paging parameters (e.g., page size, page number).
     * @return a {@link CustomPage} of tasks, containing a list of tasks and pagination details.
     */
    @Override
    public CustomPage<Task> getAllTasks(final CustomPagingRequest customPagingRequest) {

        Page<TaskEntity> taskEntitiesListPage = taskRepository.findAll(customPagingRequest.toPageable());

        if (taskEntitiesListPage.getContent().isEmpty()) {
            throw new TaskNotFoundException("Couldn't find any Task");
        }

        final List<Task> productDomainModels = listTaskEntityToListTaskMapper
                .toTaskList(taskEntitiesListPage.getContent());

        return CustomPage.of(productDomainModels, taskEntitiesListPage);

    }

    /**
     * Retrieves a task by its name.
     *
     * @param getTaskByNameRequest the request object containing the name of the task to be retrieved.
     * @return the {@link Task} entity with the specified name.
     */
    @Override
    public Task getTaskByName(final GetTaskByNameRequest getTaskByNameRequest) {

        TaskEntity taskFromDb = taskRepository.findTaskByName(getTaskByNameRequest.getName())
                .orElseThrow(()->new TaskNotFoundException("Task given name cant found"));
        
        return taskEntityToTaskMapper.map(taskFromDb);

    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to be retrieved.
     * @return the {@link Task} entity with the specified ID.
     */
    @Override
    public Task getTaskById(String id) {

        TaskEntity taskFromDb = taskRepository.findById(id)
                .orElseThrow(()->new TaskNotFoundException("Task given id cant found"));

        return taskEntityToTaskMapper.map(taskFromDb);

    }

    /**
     * Updates an existing task by its ID.
     *
     * @param id the ID of the task to be updated.
     * @param updateTaskRequest the request object containing the updated details of the task.
     * @return the updated {@link Task} entity.
     */
    @Override
    public Task updateTaskById(final String id, final UpdateTaskRequest updateTaskRequest) {

        checkTaskNameUniqueness(updateTaskRequest.getName());

        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(()->new TaskNotFoundException("Task given id cant found"));

        updateTaskRequestToTaskEntityMapper.updateTaskMapper(taskEntity,updateTaskRequest);

        TaskEntity updatedTask = taskRepository.save(taskEntity);

        return taskEntityToTaskMapper.map(updatedTask);

    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to be deleted.
     */
    @Override
    public void deleteTaskById(final String id) {

        TaskEntity taskToBeDeleted = taskRepository.findById(id)
                .orElseThrow(()->new TaskNotFoundException("With given id = " + id));

        taskRepository.delete(taskToBeDeleted);
    }

    /**
     * Checks the uniqueness of a task name in the repository.
     * If a task with the given name already exists, a {@link TaskWithThisNameAlreadyExistException} is thrown.
     *
     * @param taskName the name of the task to be checked for uniqueness.
     * @throws TaskWithThisNameAlreadyExistException if a task with the given name already exists in the repository.
     */
    private void checkTaskNameUniqueness(final String taskName) {
        if (taskRepository.existsByName(taskName)) {
            throw new TaskWithThisNameAlreadyExistException("With given task name = " + taskName);
        }
    }

}
