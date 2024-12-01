package com.example.todowithcouchbase.task.service.impl;

import com.example.todowithcouchbase.base.AbstractBaseServiceTest;
import com.example.todowithcouchbase.builder.SaveTaskRequestBuilder;
import com.example.todowithcouchbase.builder.TaskEntityBuilder;
import com.example.todowithcouchbase.builder.UpdateTaskRequestBuilder;
import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.CustomPaging;
import com.example.todowithcouchbase.task.exception.TaskNotFoundException;
import com.example.todowithcouchbase.task.exception.TaskWithThisNameAlreadyExistException;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.GetTaskByNameRequest;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.dto.request.TaskPagingRequest;
import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import com.example.todowithcouchbase.task.model.mapper.ListTaskEntityToListTaskMapper;
import com.example.todowithcouchbase.task.model.mapper.SaveTaskRequestToTaskEntityMapper;
import com.example.todowithcouchbase.task.model.mapper.TaskEntityToTaskMapper;
import com.example.todowithcouchbase.task.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

class TaskServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;


    private final SaveTaskRequestToTaskEntityMapper saveTaskRequestToTaskEntityMapper =
            SaveTaskRequestToTaskEntityMapper.initialize();

    private final TaskEntityToTaskMapper taskEntityToTaskMapper=
            TaskEntityToTaskMapper.initialize();

    private final ListTaskEntityToListTaskMapper listTaskEntityToListTaskMapper =
            ListTaskEntityToListTaskMapper.initialize();

    @Test
    void givenValidTaskCreateRequest_whenCreateTask_ThenReturnTaskResponse(){

        // Given
        final SaveTaskRequest request = new SaveTaskRequestBuilder()
                .withValidFields().build();

        final TaskEntity mockTaskEntity = saveTaskRequestToTaskEntityMapper.mapForSaving(request);

        final Task mockTask = taskEntityToTaskMapper.map(mockTaskEntity);

        // When
        Mockito.when(taskRepository.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(taskRepository.save(any(TaskEntity.class))).thenReturn(mockTaskEntity);

        // Then
        Task response = taskService.saveTaskToDatabase(request);

        Assertions.assertEquals(mockTask.getName(),response.getName());

        // Verify
        Mockito.verify(taskRepository,Mockito.times(1)).save(any(TaskEntity.class));
        Mockito.verify(taskRepository, Mockito.times(1)).existsByName(Mockito.anyString());

    }

    @Test
    void givenValidTaskCreateRequest_whenCreateTask_ThenThrowTaskWithThisNameAlreadyExistException(){

        // Given
        final SaveTaskRequest request = new SaveTaskRequestBuilder()
                .withValidFields().build();

        // When
        Mockito.when(taskRepository.existsByName(request.getName())).thenReturn(true);

        // Then
        Assertions.assertThrowsExactly(TaskWithThisNameAlreadyExistException.class,()->taskService.saveTaskToDatabase(request));

        // Verify
        Mockito.verify(taskRepository,Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(taskRepository,Mockito.times(0)).save(any(TaskEntity.class));

    }

    @Test
    void givenTaskPagingRequest_WhenTaskPageList_ThenReturnCustomPageTaskList() {

        // Given
        final TaskPagingRequest pagingRequest = TaskPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageSize(1)
                                .pageNumber(1)
                                .build()
                ).build();

        final TaskEntity taskEntity = new TaskEntityBuilder().withValidFields();

        Page<TaskEntity> taskEntityPage = new PageImpl<>(Collections.singletonList(taskEntity));

        List<Task> products = listTaskEntityToListTaskMapper.toTaskList(taskEntityPage.getContent());

        CustomPage<Task> expected = CustomPage.of(products, taskEntityPage);

        // When
        Mockito.when(taskRepository.findAll(any(Pageable.class))).thenReturn(taskEntityPage);

        // Then
        CustomPage<Task> result = taskService.getAllTasks(pagingRequest);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getContent().isEmpty());
        Assertions.assertEquals(expected.getPageNumber(), result.getPageNumber());
        Assertions.assertEquals(expected.getContent().get(0).getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(expected.getTotalPageCount(), result.getTotalPageCount());
        Assertions.assertEquals(expected.getTotalElementCount(), result.getTotalElementCount());

        // Verify
        Mockito.verify(taskRepository, Mockito.times(1)).findAll(any(Pageable.class));

    }

    @Test
    void givenTaskPagingRequest_WhenNoTaskPageList_ThenThrowTaskNotFoundException() {

        // Given
        final TaskPagingRequest pagingRequest = TaskPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageSize(1)
                                .pageNumber(1)
                                .build()
                ).build();

        Page<TaskEntity> productEntityPage = new PageImpl<>(Collections.emptyList());

        // When
        Mockito.when(taskRepository.findAll(any(Pageable.class))).thenReturn(productEntityPage);

        // Then
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.getAllTasks(pagingRequest));

        // Verify
        Mockito.verify(taskRepository, Mockito.times(1)).findAll(any(Pageable.class));

    }

    @Test
    void givenValidTaskName_whenGetTaskByName_thenReturnTaskResponse() {

        // Given
        final String taskName = "Test Task";
        final GetTaskByNameRequest request = new GetTaskByNameRequest(taskName);

        final TaskEntity mockTaskEntity = new TaskEntityBuilder()
                .withName(taskName)
                .build();

        final Task expectedTask = taskEntityToTaskMapper.map(mockTaskEntity);

        // When
        Mockito.when(taskRepository.findTaskByName(taskName))
                .thenReturn(Optional.of(mockTaskEntity));

        // Then
        Task response = taskService.getTaskByName(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedTask.getName(), response.getName());

        // Verify
        Mockito.verify(taskRepository, Mockito.times(1)).findTaskByName(taskName);

    }

    @Test
    void givenInvalidTaskName_whenGetTaskByName_thenThrowTaskNotFoundException() {

        // Given
        final String taskName = "Invalid Task";
        final GetTaskByNameRequest request = new GetTaskByNameRequest(taskName);

        // When
        Mockito.when(taskRepository.findTaskByName(taskName)).thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(TaskNotFoundException.class,
                () -> taskService.getTaskByName(request));

        // Verify
        Mockito.verify(taskRepository, Mockito.times(1)).findTaskByName(taskName);

    }

    @Test
    void givenExistId_whenGetById_thenReturnTaskResponse() {

        // Given
        final String mockId = UUID.randomUUID().toString();

        final TaskEntity mockTaskEntity = new TaskEntityBuilder()
                .withId(mockId)
                .withValidFields();

        // When
        Mockito.when(taskRepository.findById(mockId))
                .thenReturn(Optional.of(mockTaskEntity));

        // Then
        final Task expected = taskService.getTaskById(mockId);

        Assertions.assertNotNull(expected);
        Assertions.assertEquals(mockTaskEntity.getId(),expected.getId());
        Assertions.assertEquals(mockTaskEntity.getName(),expected.getName());

        // Verify
        Mockito.verify(taskRepository, Mockito.times(1)).findById(mockId);

    }

    @Test
    void givenNonExistId_whenGetById_thenThrowTaskNotFoundException() {

        // Given
        final String mockId = UUID.randomUUID().toString();

        // When
        Mockito.when(taskRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(TaskNotFoundException.class,
                ()->taskService.getTaskById(mockId));

        // Verify
        Mockito.verify(taskRepository, Mockito.times(1)).findById(mockId);

    }

    @Test
    void givenValidUpdateTaskRequest_whenUpdateTaskRequest_thenReturnTaskResponse(){

        // Given
        final String mockId = UUID.randomUUID().toString();

        final UpdateTaskRequest mockUpdateTaskRequest = new UpdateTaskRequestBuilder()
                .withValidFields()
                .build();

        final TaskEntity mockTaskEntityBeforeUpdate = new TaskEntityBuilder()
                .withName("beforeTask")
                .build();

        final TaskEntity mockTaskEntityAfterUpdate = new TaskEntityBuilder()
                .withName("updateTask")
                .build();

        final Task mockUpdatedTask = taskEntityToTaskMapper.map(mockTaskEntityAfterUpdate);

        // When
        Mockito.when(taskRepository.findById(mockId))
                .thenReturn(Optional.of(mockTaskEntityBeforeUpdate));

        Mockito.when(taskRepository.save(mockTaskEntityBeforeUpdate))
                .thenReturn(mockTaskEntityAfterUpdate);

        // Then
        final Task taskResponse = taskService.updateTaskById(mockId, mockUpdateTaskRequest);

        Assertions.assertNotNull(taskResponse);
        Assertions.assertEquals(mockUpdatedTask.getName(), taskResponse.getName());

        // Verify
        Mockito.verify(taskRepository, Mockito.times(1)).findById(mockId);
        Mockito.verify(taskRepository, Mockito.times(1)).save(mockTaskEntityBeforeUpdate);

    }

    @Test
    void givenEmptyUpdateTaskRequest_whenUpdateTaskRequest_thenThrowNotFoundException(){

        // Given
        final String mockId = UUID.randomUUID().toString();

        final UpdateTaskRequest mockUpdateTaskRequest = new UpdateTaskRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(taskRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(TaskNotFoundException.class,
                ()->taskService.updateTaskById(mockId,mockUpdateTaskRequest));

        // Verify
        Mockito.verify(taskRepository,Mockito.times(1)).findById(mockId);
        Mockito.verify(taskRepository,Mockito.never()).save(Mockito.any(TaskEntity.class));

    }

}
