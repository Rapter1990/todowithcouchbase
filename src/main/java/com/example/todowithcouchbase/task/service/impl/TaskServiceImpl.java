package com.example.todowithcouchbase.task.service.impl;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.request.CustomPagingRequest;
import com.example.todowithcouchbase.task.exception.TaskWithThisNameAlreadyExistException;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import com.example.todowithcouchbase.task.model.mapper.task.SaveTaskRequestToTaskEntityMapper;
import com.example.todowithcouchbase.task.model.mapper.task.TaskEntityToTaskMapper;
import com.example.todowithcouchbase.task.repository.TaskRepository;
import com.example.todowithcouchbase.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Task getById(final String id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(()->new RuntimeException("task cant found given id"));
        Task taskModel = taskEntityToTaskMapper.map(task);
        String nameToBeChanged = taskModel.getName();


        return null;
    }

    @Override
    public CustomPage<Task> getAllTask(
            final CustomPagingRequest customPagingRequest
            ) {

        Page<TaskEntity> taskEntitiesListPage = taskRepository.findAll(customPagingRequest.toPageable());

        List<Task> taskDomainModel = taskEntityToTaskMapper.map(taskEntitiesListPage.toList());
        return CustomPage.of(
                taskDomainModel,
                taskEntitiesListPage
        );

    }

    private boolean isNameExist(final String name){
        return !Boolean.TRUE.equals(taskRepository.existsByName(name));
    }


}
