package com.example.todowithcouchbase.task.controller;

import com.example.todowithcouchbase.base.AbstractRestControllerTest;
import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.CustomPaging;
import com.example.todowithcouchbase.common.model.dto.response.CustomPagingResponse;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.dto.request.TaskPagingRequest;
import com.example.todowithcouchbase.task.model.dto.response.TaskResponse;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import com.example.todowithcouchbase.task.model.mapper.CustomPageTaskToCustomPagingTaskResponseMapper;
import com.example.todowithcouchbase.task.model.mapper.TaskToTaskResponseMapper;
import com.example.todowithcouchbase.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

class TaskControllerTest extends AbstractRestControllerTest {

    @MockBean
    private TaskService taskService;

    private final TaskToTaskResponseMapper taskToTaskResponseMapper =  TaskToTaskResponseMapper.initialize();

    private final CustomPageTaskToCustomPagingTaskResponseMapper customPageTaskToCustomPagingTaskResponseMapper =
            CustomPageTaskToCustomPagingTaskResponseMapper.initialize();

    @Test
    void givenValidTaskRequestWithAdminCreate_whenCreateTask_thenSuccess() throws Exception{

        //Given
        final SaveTaskRequest request = SaveTaskRequest.builder()
                .name("task-name")
                .build();

        final Task expectedTask = Task.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .build();

        //When
        Mockito.when(taskService.saveTaskToDatabase(any(SaveTaskRequest.class)))
                .thenReturn(expectedTask);

        //Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + mockAdminToken.getAccessToken())

        ).andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value(expectedTask.getId()));

        //Verify
        Mockito.verify(taskService,Mockito.times(1))
                .saveTaskToDatabase(any(SaveTaskRequest.class));

    }

    @Test
    void givenValidTaskRequestWhenWithUserCreateThenThrowUnAuthorizeException() throws Exception{

        //Given
        final SaveTaskRequest request = SaveTaskRequest.builder()
                .name("task-name")
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // Verify
        Mockito.verify(taskService, Mockito.never()).saveTaskToDatabase(any(SaveTaskRequest.class));

    }

    @Test
    void givenValidTaskRequest_whenForbiddenThroughUser_thenThrowForbidden() throws Exception {

        // Given
        final SaveTaskRequest request = SaveTaskRequest.builder()
                .name("task-name")
                .build();

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        // Verify
        Mockito.verify(taskService, Mockito.never())
                .saveTaskToDatabase(any(SaveTaskRequest.class));

    }

    @Test
    void givenTaskPagingRequest_whenGetTasksFromAdmin_thenReturnCustomPageTask() throws Exception {

        // Given
        TaskPagingRequest pagingRequest = TaskPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageSize(1)
                                .pageNumber(1)
                                .build()
                ).build();

        String taskId = UUID.randomUUID().toString();

        TaskEntity expectedEntity = TaskEntity.builder()
                .id(taskId)
                .name("Test Task")
                .build();

        List<TaskEntity> taskEntities = List.of(expectedEntity);

        Page<TaskEntity> taskEntityPage = new PageImpl<>(taskEntities, PageRequest.of(1, 1), taskEntities.size());

        List<Task> taskDomainModels = taskEntities.stream()
                .map(entity -> new Task(entity.getId(), entity.getName()))
                .collect(Collectors.toList());

        CustomPage<Task> taskPage = CustomPage.of(taskDomainModels, taskEntityPage);

        CustomPagingResponse<TaskResponse> expectedResponse = customPageTaskToCustomPagingTaskResponseMapper.toPagingResponse(taskPage);

        // When
        Mockito.when(taskService.getAllTasks(any(TaskPagingRequest.class))).thenReturn(taskPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].id").value(expectedResponse.getContent().get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].name").value(expectedResponse.getContent().get(0).getName()));

        // Verify
        Mockito.verify(taskService, Mockito.times(1)).getAllTasks(any(TaskPagingRequest.class));

    }

    @Test
    void givenTaskPagingRequest_whenGetTasksFromUser_thenReturnCustomPageTask() throws Exception {

        // Given
        TaskPagingRequest pagingRequest = TaskPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageSize(1)
                                .pageNumber(1)
                                .build()
                ).build();

        String taskId = UUID.randomUUID().toString();

        TaskEntity expectedEntity = TaskEntity.builder()
                .id(taskId)
                .name("Test Task")
                .build();

        List<TaskEntity> taskEntities = List.of(expectedEntity);

        Page<TaskEntity> taskEntityPage = new PageImpl<>(taskEntities, PageRequest.of(1, 1), taskEntities.size());

        List<Task> taskDomainModels = taskEntities.stream()
                .map(entity -> new Task(entity.getId(), entity.getName()))
                .collect(Collectors.toList());

        CustomPage<Task> taskPage = CustomPage.of(taskDomainModels, taskEntityPage);

        CustomPagingResponse<TaskResponse> expectedResponse = customPageTaskToCustomPagingTaskResponseMapper.toPagingResponse(taskPage);

        // When
        Mockito.when(taskService.getAllTasks(any(TaskPagingRequest.class))).thenReturn(taskPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].id").value(expectedResponse.getContent().get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].name").value(expectedResponse.getContent().get(0).getName()));

        // Verify
        Mockito.verify(taskService, Mockito.times(1)).getAllTasks(any(TaskPagingRequest.class));
    }

    @Test
    void givenTaskPagingRequest_WhenUnauthorized_thenThrowUnauthorized() throws Exception {

        // Given
        TaskPagingRequest pagingRequest = TaskPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageSize(1)
                                .pageNumber(1)
                                .build()
                ).build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // Verify
        Mockito.verify(taskService, Mockito.never()).getAllTasks(any(TaskPagingRequest.class));

    }

}
