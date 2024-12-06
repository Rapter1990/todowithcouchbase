package com.example.todowithcouchbase.task.controller;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.response.CustomPagingResponse;
import com.example.todowithcouchbase.common.model.dto.response.CustomResponse;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.GetTaskByNameRequest;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.dto.request.TaskPagingRequest;
import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;
import com.example.todowithcouchbase.task.model.dto.response.TaskResponse;
import com.example.todowithcouchbase.task.model.mapper.CustomPageTaskToCustomPagingTaskResponseMapper;
import com.example.todowithcouchbase.task.model.mapper.TaskToTaskResponseMapper;
import com.example.todowithcouchbase.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing tasks.
 * Provides endpoints for task creation, retrieval, update, and deletion.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskToTaskResponseMapper taskToTaskResponseMapper =  TaskToTaskResponseMapper.initialize();

    private final CustomPageTaskToCustomPagingTaskResponseMapper customPageTaskToCustomPagingTaskResponseMapper =
            CustomPageTaskToCustomPagingTaskResponseMapper.initialize();

    /**
     * Creates a new task and saves it to the database.
     *
     * @param saveTaskRequest the request body containing the task details to be created.
     * @return a response containing the ID of the created task.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<String> saveTask(@RequestBody @Valid final SaveTaskRequest saveTaskRequest){
        final Task createdTask = taskService.saveTaskToDatabase(saveTaskRequest);

        return CustomResponse.successOf(createdTask.getId());
    }

    /**
     * Retrieves a paginated list of tasks.
     *
     * @param request the request body containing pagination and sorting information.
     * @return a paginated response containing a list of tasks.
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<CustomPagingResponse<TaskResponse>> getAllTasks(@RequestBody @Valid final TaskPagingRequest request){
        final CustomPage<Task> taskPage= taskService.getAllTasks(request);

        final CustomPagingResponse<TaskResponse> response = customPageTaskToCustomPagingTaskResponseMapper
                .toPagingResponse(taskPage);

        return CustomResponse.successOf(response);
    }

    /**
     * Retrieves a task by its name.
     *
     * @param getTaskByNameRequest the request body containing the task name to search for.
     * @return a response containing the task details.
     */
    @PostMapping("/getByName")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<TaskResponse> getTaskByName(@RequestBody @Valid final GetTaskByNameRequest getTaskByNameRequest){
        final Task task = taskService.getTaskByName(getTaskByNameRequest);

        final TaskResponse response = taskToTaskResponseMapper.map(task);

        return CustomResponse.successOf(response);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to be retrieved.
     * @return a response containing the task details.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<TaskResponse> getTaskById(@PathVariable @Valid @UUID final String id){
        final Task task = taskService.getTaskById(id);

        final TaskResponse response = taskToTaskResponseMapper.map(task);

        return CustomResponse.successOf(response);
    }

    /**
     * Updates an existing task by its ID.
     *
     * @param id the ID of the task to be updated.
     * @param updateTaskRequest the request body containing the updated task details.
     * @return a response containing the updated task details.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public CustomResponse<TaskResponse> updateTaskById(
            @PathVariable @Valid @UUID final String id,
            @RequestBody @Valid final UpdateTaskRequest updateTaskRequest){

        final Task task = taskService.updateTaskById(id, updateTaskRequest);

        final TaskResponse response = taskToTaskResponseMapper.map(task);

        return CustomResponse.successOf(response);

    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to be deleted.
     * @return a response containing a success message.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public CustomResponse<String> deleteTaskById(@PathVariable @Valid final String id){

        taskService.deleteTaskById(id);

        return CustomResponse.successOf("Task with id "+ id + "is deleted");

    }

}


