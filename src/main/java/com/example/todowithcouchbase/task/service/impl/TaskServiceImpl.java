package com.example.todowithcouchbase.task.service.impl;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.request.CustomPagingRequest;
import com.example.todowithcouchbase.task.exception.TaskNotFoundException;
import com.example.todowithcouchbase.task.exception.TaskWithThisNameAlreadyExistException;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.GetTaskByNameRequest;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import com.example.todowithcouchbase.task.model.mapper.ListTaskEntityToListTaskMapper;
import com.example.todowithcouchbase.task.model.mapper.SaveTaskRequestToTaskEntityMapper;
import com.example.todowithcouchbase.task.model.mapper.TaskEntityToTaskMapper;
import com.example.todowithcouchbase.task.model.mapper.UpdateTaskRequestToTaskEntityMapper;
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

    private final ListTaskEntityToListTaskMapper listTaskEntityToListTaskMapper =
            ListTaskEntityToListTaskMapper.initialize();

    private final UpdateTaskRequestToTaskEntityMapper updateTaskRequestToTaskEntityMapper =
            UpdateTaskRequestToTaskEntityMapper.initialize();


    @Override
    public Task saveTaskToDatabase(final SaveTaskRequest taskRequest) {

        checkTaskNameUniqueness(taskRequest.getName());

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

    @Override
    public Task updateTaskById(final String id, final UpdateTaskRequest updateTaskRequest) {

        checkTaskNameUniqueness(updateTaskRequest.getName());

        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(()->new TaskNotFoundException("Task given id cant found"));

        updateTaskRequestToTaskEntityMapper.updateTaskMapper(taskEntity,updateTaskRequest);

        TaskEntity updatedTask = taskRepository.save(taskEntity);

        return taskEntityToTaskMapper.map(updatedTask);
    }

    @Override
    public void deleteTaskById(final String id) {

        TaskEntity taskToBeDeleted = taskRepository.findById(id)
                .orElseThrow(()->new TaskNotFoundException("Task given id cant found"));

        taskRepository.delete(taskToBeDeleted);
    }

    private void checkTaskNameUniqueness(final String taskName) {
        if (taskRepository.existsByName(taskName)) {
            throw new TaskWithThisNameAlreadyExistException("With given task name = " + taskName);
        }
    }

}
