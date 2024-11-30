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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskToTaskResponseMapper taskToTaskResponseMapper =  TaskToTaskResponseMapper.initialize();

    private final CustomPageTaskToCustomPagingTaskResponseMapper customPageTaskToCustomPagingTaskResponseMapper =
            CustomPageTaskToCustomPagingTaskResponseMapper.initialize();

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<String> saveTask(final @RequestBody @Valid SaveTaskRequest saveTaskRequest){
        final Task createdTask = taskService.saveTaskToDatabase(saveTaskRequest);

        return CustomResponse.successOf(createdTask.getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<CustomPagingResponse<TaskResponse>> getAllTasks(final @RequestBody @Valid TaskPagingRequest request){
        final CustomPage<Task> taskPage= taskService.getAllTasks(request);

        final CustomPagingResponse<TaskResponse> response = customPageTaskToCustomPagingTaskResponseMapper
                .toPagingResponse(taskPage);

        return CustomResponse.successOf(response);
    }

    @PostMapping("/getByName")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<TaskResponse> getTaskByName(final @RequestBody @Valid GetTaskByNameRequest getTaskByNameRequest){
        final Task task = taskService.getTaskByName(getTaskByNameRequest);

        final TaskResponse response = taskToTaskResponseMapper.map(task);

        return CustomResponse.successOf(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<TaskResponse> getTaskById(@PathVariable @UUID final String id){
        final Task task = taskService.getTaskById(id);

        final TaskResponse response = taskToTaskResponseMapper.map(task);

        return CustomResponse.successOf(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public CustomResponse<TaskResponse> updateTaskById(
            @PathVariable @UUID final String id,
            @RequestBody @Valid UpdateTaskRequest updateTaskRequest){

        final Task task =taskService.updateTaskById(id, updateTaskRequest);

        final TaskResponse response = taskToTaskResponseMapper.map(task);

        return CustomResponse.successOf(response);
    }

}


