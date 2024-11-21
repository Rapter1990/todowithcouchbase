package com.example.todowithcouchbase.task.controller;

import com.example.todowithcouchbase.base.AbstractRestControllerTest;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.mapper.task.TaskToTaskResponseMapper;
import com.example.todowithcouchbase.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

class TaskControllerTest extends AbstractRestControllerTest {

    @MockBean
    private TaskService taskService;

    private final TaskToTaskResponseMapper taskToTaskResponseMapper =  TaskToTaskResponseMapper.initialize();

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
        Mockito.when(taskService.saveTaskToDatabase(Mockito.any(SaveTaskRequest.class)))
                .thenReturn(expectedTask);

        //Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/task")
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
                .saveTaskToDatabase(Mockito.any(SaveTaskRequest.class));

    }

    @Test
    void givenValidTaskRequestWhenWithUserCreateThenThrowUnAuthorizeException() throws Exception{

        //Given
        final SaveTaskRequest request = SaveTaskRequest.builder()
                .name("task-name")
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/task")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // Verify
        Mockito.verify(taskService, Mockito.never()).saveTaskToDatabase(Mockito.any(SaveTaskRequest.class));

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
                                .post("/api/v1/task")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        // Verify
        Mockito.verify(taskService, Mockito.never())
                .saveTaskToDatabase(Mockito.any(SaveTaskRequest.class));

    }

}
