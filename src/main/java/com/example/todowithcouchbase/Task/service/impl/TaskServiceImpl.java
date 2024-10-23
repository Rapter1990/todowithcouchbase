package com.example.todowithcouchbase.Task.service.impl;

import com.example.todowithcouchbase.Task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.Task.model.dto.response.SaveTaskResponse;
import com.example.todowithcouchbase.Task.model.entity.TaskEntity;
import com.example.todowithcouchbase.Task.model.mapper.SaveTaskRequestToTaskEntityMapper;
import com.example.todowithcouchbase.Task.model.mapper.TaskEntityToTaskResponseMapper;
import com.example.todowithcouchbase.Task.repository.TaskRepository;
import com.example.todowithcouchbase.Task.service.TaskService;
import com.example.todowithcouchbase.Task.exception.TaskWithThisNameAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final SaveTaskRequestToTaskEntityMapper saveTaskRequestToTaskEntityMapper =
            SaveTaskRequestToTaskEntityMapper.initialize();

    private final TaskEntityToTaskResponseMapper taskEntityToTaskResponseMapper=
            TaskEntityToTaskResponseMapper.initialize();

    @Override
    public SaveTaskResponse saveTaskToDatabase(final SaveTaskRequest taskRequest) {
        if (!isNameExist(taskRequest.getName())){
            throw new TaskWithThisNameAlreadyExistException("Task with this name already exist");
        }
        TaskEntity taskEntityToBeSaved=saveTaskRequestToTaskEntityMapper.mapForSaving(taskRequest);
        taskRepository.save(taskEntityToBeSaved);
        return taskEntityToTaskResponseMapper.map(taskEntityToBeSaved);
    }

    private boolean isNameExist(final String name){
        return !Boolean.TRUE.equals(taskRepository.existsTaskEntitiesByName(name));
    }
}
