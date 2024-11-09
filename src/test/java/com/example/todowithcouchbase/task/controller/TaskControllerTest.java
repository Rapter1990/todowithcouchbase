package com.example.todowithcouchbase.task.controller;

import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.dto.response.SaveTaskResponse;
import com.example.todowithcouchbase.task.service.TaskService;
import com.example.todowithcouchbase.base.AbstractRestControllerTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

public class TaskControllerTest extends AbstractRestControllerTest {

    @MockBean
    private TaskService taskService;

    @Test
    void givenValidTaskRequestWithAdminCreate_whenCreateTask_thenSuccess() throws Exception{

        //Given
        final SaveTaskRequest request = SaveTaskRequest.builder()
                .name("deneme")
                .build();

        final SaveTaskResponse response = SaveTaskResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("deneme")
                .build();

        //When
        Mockito.when(taskService.saveTaskToDatabase(Mockito.any(SaveTaskRequest.class))).thenReturn(response);

        //Then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION,mockAdminToken)

        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name").value(response.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        //Verify
        Mockito.verify(taskService,Mockito.times(1))
                .saveTaskToDatabase(Mockito.any(SaveTaskRequest.class));
    }

    @Test
    void givenValidTaskRequestWhenWithUserCreateThenThrowUnAuthorizeException() throws Exception{

        //Given
        final SaveTaskRequest request = SaveTaskRequest.builder()
                .name("deneme")
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
}
