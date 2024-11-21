package com.example.todowithcouchbase.task.service.impl;

import com.example.todowithcouchbase.base.AbstractBaseServiceTest;
import com.example.todowithcouchbase.builder.SaveTaskRequestBuilder;
import com.example.todowithcouchbase.task.exception.TaskWithThisNameAlreadyExistException;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import com.example.todowithcouchbase.task.model.mapper.SaveTaskRequestToTaskEntityMapper;
import com.example.todowithcouchbase.task.model.mapper.TaskEntityToTaskMapper;
import com.example.todowithcouchbase.task.repository.TaskRepository;
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


    private final SaveTaskRequestToTaskEntityMapper saveTaskRequestToTaskEntityMapper =
            SaveTaskRequestToTaskEntityMapper.initialize();

    private final TaskEntityToTaskMapper taskEntityToTaskResponseMapper=
            TaskEntityToTaskMapper.initialize();

    @Test
    void givenValidTaskCreateRequest_whenCreateTask_ThenReturnTaskResponse(){

        //Given
        final SaveTaskRequest request = new SaveTaskRequestBuilder()
                .withValidFields().build();

        final TaskEntity mockTaskEntity = saveTaskRequestToTaskEntityMapper.mapForSaving(request);

        final Task mockTask = taskEntityToTaskResponseMapper.map(mockTaskEntity);

        //When
        Mockito.when(taskRepository.existsByName(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class)))
                .thenReturn(mockTaskEntity);

        //Then
        Task response = taskService
                .saveTaskToDatabase(request);

        Assertions.assertEquals(mockTask.getName(),response.getName());

        //Verify
        Mockito.verify(
                taskRepository,
                Mockito.times(1)
        ).save(Mockito.any(TaskEntity.class));


        System.out.println();

        Mockito.verify(
                taskRepository,
                Mockito.times(1)
        ).existsByName(Mockito.anyString());
    }

    @Test
    void givenValidTaskCreateRequest_whenCreateTask_ThenThrowTaskWithThisNameAlreadyExistException(){

        //Given
        final SaveTaskRequest request = new SaveTaskRequestBuilder()
                .withValidFields().build();

        //When
        Mockito.when(taskRepository.existsByName(request.getName()))
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
        ).existsByName(Mockito.anyString());

        Mockito.verify(
                taskRepository,
                Mockito.times(0)
        ).save(Mockito.any(TaskEntity.class));

    }

}
