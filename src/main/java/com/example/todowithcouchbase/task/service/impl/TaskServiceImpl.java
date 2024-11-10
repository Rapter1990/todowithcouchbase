package com.example.todowithcouchbase.task.service.impl;

import com.example.todowithcouchbase.task.exception.TaskWithThisNameAlreadyExistException;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import com.example.todowithcouchbase.task.model.mapper.SaveTaskRequestToTaskEntityMapper;
import com.example.todowithcouchbase.task.model.mapper.TaskEntityToTaskMapper;
import com.example.todowithcouchbase.task.repository.TaskRepository;
import com.example.todowithcouchbase.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final SaveTaskRequestToTaskEntityMapper saveTaskRequestToTaskEntityMapper =
            SaveTaskRequestToTaskEntityMapper.initialize();

    private final TaskEntityToTaskMapper taskEntityToTaskMapper =
            TaskEntityToTaskMapper.initialize();

    @Override
    public Task saveTaskToDatabase(final SaveTaskRequest taskRequest) {
        if (!isNameExist(taskRequest.getName())){
            throw new TaskWithThisNameAlreadyExistException("Task with this name already exist");
        }
        TaskEntity taskEntityToBeSaved=saveTaskRequestToTaskEntityMapper.mapForSaving(taskRequest);
        taskRepository.save(taskEntityToBeSaved);
        return taskEntityToTaskMapper.map(taskEntityToBeSaved);
    }

    private boolean isNameExist(final String name){
        return !Boolean.TRUE.equals(taskRepository.existsByName(name));
    }
}
