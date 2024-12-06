package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Unit test class for {@link UpdateTaskRequestToTaskEntityMapperImpl}.
 * This class validates the mapping logic from {@link UpdateTaskRequest} to {@link TaskEntity}.
 */
class UpdateTaskRequestToTaskEntityMapperTest {

    private final UpdateTaskRequestToTaskEntityMapperImpl mapper = new UpdateTaskRequestToTaskEntityMapperImpl();

    @Test
    void testMapWithNullRequest() {

        TaskEntity result = mapper.map((UpdateTaskRequest) null);

        assertNull(result);

    }

    @Test
    void testMapWithValidRequest() {

        UpdateTaskRequest updateRequest = UpdateTaskRequest.builder()
                .name("Updated Task")
                .build();

        TaskEntity result = mapper.map(updateRequest);

        assertNotNull(result);
        assertEquals("Updated Task", result.getName());

    }

    @Test
    void testMapWithEmptyValues() {

        UpdateTaskRequest updateRequest = UpdateTaskRequest.builder()
                .name("")
                .build();

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

        UpdateTaskRequest request1 = UpdateTaskRequest.builder()
                .name("Task 1")
                .build();

        UpdateTaskRequest request2 = UpdateTaskRequest.builder()
                .name("Task 2")
                .build();

        List<TaskEntity> result = mapper.map(Arrays.asList(request1, request2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getName());
        assertEquals("Task 2", result.get(1).getName());

    }

    @Test
    void testMapCollectionWithNullElements() {

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .name("Task 1")
                .build();

        List<TaskEntity> result = mapper.map(Arrays.asList(request, null));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getName());
        assertNull(result.get(1));
    }

    @Test
    void testMapWithEdgeCaseValues() {

        UpdateTaskRequest updateRequest = UpdateTaskRequest.builder()
                .name(null)
                .build();

        TaskEntity result = mapper.map(updateRequest);

        assertNotNull(result);
        assertNull(result.getName());

    }

}
