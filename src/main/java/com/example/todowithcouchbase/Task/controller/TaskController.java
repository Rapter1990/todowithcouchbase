package com.example.todowithcouchbase.Task.controller;

import com.example.todowithcouchbase.Task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.Task.model.dto.response.SaveTaskResponse;
import com.example.todowithcouchbase.Task.service.TaskService;
import com.example.todowithcouchbase.common.model.dto.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.Role;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/task")
@Validated
public class TaskController {

    private final TaskService taskService;
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<SaveTaskResponse> saveTask(final @Valid @RequestBody SaveTaskRequest saveTaskRequest){
        return CustomResponse.successOf(taskService.saveTaskToDatabase(saveTaskRequest));
    }
}
