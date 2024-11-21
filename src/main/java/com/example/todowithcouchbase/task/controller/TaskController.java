package com.example.todowithcouchbase.task.controller;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.response.CustomPagingResponse;
import com.example.todowithcouchbase.common.model.dto.response.CustomResponse;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.dto.request.TaskPagingRequest;
import com.example.todowithcouchbase.task.model.dto.response.TaskResponse;
import com.example.todowithcouchbase.task.model.mapper.CustomPageTaskToCustomPagingTaskResponseMapper;
import com.example.todowithcouchbase.task.model.mapper.TaskToTaskResponseMapper;
import com.example.todowithcouchbase.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public CustomResponse<String> saveTask(final @Valid @RequestBody SaveTaskRequest saveTaskRequest){
        final Task createdTask = taskService.saveTaskToDatabase(saveTaskRequest);
        return CustomResponse.successOf(createdTask.getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<CustomPagingResponse<TaskResponse>> getAllTasks(final @RequestBody TaskPagingRequest request){
        final CustomPage<Task> taskPage= taskService.getAllTasks(request);

        final CustomPagingResponse<TaskResponse> response = customPageTaskToCustomPagingTaskResponseMapper
                .toPagingResponse(taskPage);

        return CustomResponse.successOf(response);
    }

}
