package com.example.todowithcouchbase.task.service.impl;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.request.CustomPagingRequest;
import com.example.todowithcouchbase.task.exception.TaskNotFoundException;
import com.example.todowithcouchbase.task.exception.TaskWithThisNameAlreadyExistException;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.GetTaskByNameRequest;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.dto.request.TaskPagingRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import com.example.todowithcouchbase.task.model.mapper.ListTaskEntityToListTaskMapper;
import com.example.todowithcouchbase.task.model.mapper.SaveTaskRequestToTaskEntityMapper;
import com.example.todowithcouchbase.task.model.mapper.TaskEntityToTaskMapper;
import com.example.todowithcouchbase.task.repository.TaskRepository;
import com.example.todowithcouchbase.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public CustomPage<Task> getAllTasks(final CustomPagingRequest customPagingRequest) {

        Page<TaskEntity> taskEntitiesListPage = taskRepository.findAll(customPagingRequest.toPageable());

        if (taskEntitiesListPage.getContent().isEmpty()) {
            throw new TaskNotFoundException("Couldn't find any Task");
        }

        final List<Task> productDomainModels = listTaskEntityToListTaskMapper
                .toTaskList(taskEntitiesListPage.getContent());

        return CustomPage.of(productDomainModels, taskEntitiesListPage);

    }

    @Override
    public Task getTaskByName(final GetTaskByNameRequest getTaskByNameRequest) {

        TaskEntity taskFromDb = taskRepository.findTaskByName(getTaskByNameRequest.getName())
                .orElseThrow(()->new TaskNotFoundException("Task given name cant found"));
        
        return taskEntityToTaskMapper.map(taskFromDb);

    }

    @Override
    public Task getTaskById(String id) {

        TaskEntity taskFromDb = taskRepository.findById(id)
                .orElseThrow(()->new TaskNotFoundException("Task given id cant found"));

        return taskEntityToTaskMapper.map(taskFromDb);
    }

    private boolean isNameExist(final String name){
        return !Boolean.TRUE.equals(taskRepository.existsByName(name));
    }


}
