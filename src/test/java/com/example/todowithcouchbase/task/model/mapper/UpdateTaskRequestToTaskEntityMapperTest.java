package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTaskRequestToTaskEntityMapperTest {

    private final UpdateTaskRequestToTaskEntityMapperImpl mapper = new UpdateTaskRequestToTaskEntityMapperImpl();

    @Test
    void testMapWithNullRequest() {
        TaskEntity result = mapper.map((UpdateTaskRequest) null);
        assertNull(result);
    }

    @Test
    void testMapWithValidRequest() {
        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setName("Updated Task");

        TaskEntity result = mapper.map(updateRequest);

        assertNotNull(result);
        assertEquals("Updated Task", result.getName());
    }

    @Test
    void testMapWithEmptyValues() {
        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setName("");

        TaskEntity result = mapper.map(updateRequest);

        assertNotNull(result);
        assertEquals("", result.getName());
    }

    @Test
    void testMapCollectionWithNull() {
        List<TaskEntity> result = mapper.map((Collection<UpdateTaskRequest>) null);
        assertNull(result);
    }

    @Test
    void testMapCollectionWithEmptyList() {
        List<TaskEntity> result = mapper.map(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMapCollectionWithValidRequests() {
        UpdateTaskRequest request1 = new UpdateTaskRequest();
        request1.setName("Task 1");

        UpdateTaskRequest request2 = new UpdateTaskRequest();
        request2.setName("Task 2");

        List<TaskEntity> result = mapper.map(Arrays.asList(request1, request2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getName());
        assertEquals("Task 2", result.get(1).getName());
    }

    @Test
    void testMapCollectionWithNullElements() {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setName("Task 1");

        List<TaskEntity> result = mapper.map(Arrays.asList(request, null));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getName());
        assertNull(result.get(1));
    }

    @Test
    void testMapWithEdgeCaseValues() {
        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setName(null);

        TaskEntity result = mapper.map(updateRequest);

        assertNotNull(result);
        assertNull(result.getName());
    }

}
