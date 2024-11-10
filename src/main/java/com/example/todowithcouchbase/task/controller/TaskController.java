package com.example.todowithcouchbase.task.controller;

import com.example.todowithcouchbase.common.model.dto.response.CustomResponse;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.mapper.TaskToTaskResponseMapper;
import com.example.todowithcouchbase.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/task")
@Validated
public class TaskController {

    private final TaskService taskService;

    private final TaskToTaskResponseMapper taskToTaskResponseMapper =  TaskToTaskResponseMapper.initialize();

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<String> saveTask(final @Valid @RequestBody SaveTaskRequest saveTaskRequest){
        final Task createdTask = taskService.saveTaskToDatabase(saveTaskRequest);
        return CustomResponse.successOf(createdTask.getId());
    }

}
