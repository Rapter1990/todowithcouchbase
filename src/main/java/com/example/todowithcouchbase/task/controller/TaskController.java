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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Task Management", description = "Endpoints for managing tasks including creation, retrieval, updating, and deletion.")
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
    @Operation(
            summary = "Create a new task",
            description = "Creates a new task and saves it to the database. Accessible by ADMIN only.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid task details provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication is required"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden")
            }
    )
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
    @Operation(
            summary = "Get all tasks",
            description = "Retrieves a paginated list of tasks. Accessible by both ADMIN and USER roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tasks successfully retrieved"),
                    @ApiResponse(responseCode = "400", description = "Invalid update details provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication is required"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
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
    @Operation(
            summary = "Get task by name",
            description = "Retrieves a task by its name. Accessible by both ADMIN and USER roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully retrieved"),
                    @ApiResponse(responseCode = "400", description = "Invalid update details provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication is required"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
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
    @Operation(
            summary = "Get task by ID",
            description = "Retrieves a task by its ID. Accessible by both ADMIN and USER roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully retrieved"),
                    @ApiResponse(responseCode = "400", description = "Invalid update details provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication is required"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
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
    @Operation(
            summary = "Update a task",
            description = "Updates an existing task by its ID. Accessible by ADMIN only.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid update details provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication is required"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
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
    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its ID. Accessible by ADMIN only.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid update details provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication is required"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public CustomResponse<String> deleteTaskById(@PathVariable @Valid final String id){

        taskService.deleteTaskById(id);

        return CustomResponse.successOf("Task with id "+ id + "is deleted");

    }

}
