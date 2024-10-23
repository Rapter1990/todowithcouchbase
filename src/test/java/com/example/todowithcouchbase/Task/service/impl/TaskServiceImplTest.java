package com.example.todowithcouchbase.Task.service.impl;

import com.example.todowithcouchbase.Task.exception.TaskWithThisNameAlreadyExistException;
import com.example.todowithcouchbase.Task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.Task.model.dto.response.SaveTaskResponse;
import com.example.todowithcouchbase.Task.model.entity.TaskEntity;
import com.example.todowithcouchbase.Task.model.mapper.SaveTaskRequestToTaskEntityMapper;
import com.example.todowithcouchbase.Task.model.mapper.TaskEntityToTaskResponseMapper;
import com.example.todowithcouchbase.Task.repository.TaskRepository;
import com.example.todowithcouchbase.base.AbstractBaseServiceTest;
import com.example.todowithcouchbase.builder.SaveTaskRequestBuilder;
import com.example.todowithcouchbase.builder.TaskEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

class TaskServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private final SaveTaskRequestToTaskEntityMapper saveTaskRequestToTaskEntityMapper =
            SaveTaskRequestToTaskEntityMapper.initialize();

    @Mock
    private final TaskEntityToTaskResponseMapper taskEntityToTaskResponseMapper=
            TaskEntityToTaskResponseMapper.initialize();

    @Test
    void givenValidTaskCreateRequest_whenCreateTask_ThenReturnTaskResponse(){

        //Given
        final SaveTaskRequest request = new SaveTaskRequestBuilder()
                .withValidFields().build();

        final TaskEntity mockTaskEntity = new TaskEntityBuilder()
                .withValidFields()
                .build();

        //When
        Mockito.when(taskRepository.existsTaskEntitiesByName(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class)))
                .thenReturn(mockTaskEntity);

        //Then
        SaveTaskResponse response = taskService
                .saveTaskToDatabase(request);

        Assertions.assertEquals(request.getName(),response.getName());

        //Verify
        Mockito.verify(
                taskRepository,
                Mockito.times(1)
        ).save(Mockito.any(TaskEntity.class));

        Mockito.verify(
                taskRepository,
                Mockito.times(1)
        ).existsTaskEntitiesByName(Mockito.anyString());
    }

    @Test
    void givenValidTaskCreateRequest_whenCreateTask_ThenThrowTaskWithThisNameAlreadyExistException(){

        //Given
        final SaveTaskRequest request = new SaveTaskRequestBuilder()
                .withValidFields().build();

        //When
        Mockito.when(taskRepository.existsTaskEntitiesByName(request.getName()))
                .thenReturn(true);

        //Then
        Assertions.assertThrowsExactly(
                TaskWithThisNameAlreadyExistException.class,
                ()->taskService.saveTaskToDatabase(request)
        );

        //Verify
        Mockito.verify(
                taskRepository,
                Mockito.times(1)
        ).existsTaskEntitiesByName(Mockito.anyString());

        Mockito.verify(
                taskRepository,
                Mockito.times(0)
        ).save(Mockito.any(TaskEntity.class));

    }

}
