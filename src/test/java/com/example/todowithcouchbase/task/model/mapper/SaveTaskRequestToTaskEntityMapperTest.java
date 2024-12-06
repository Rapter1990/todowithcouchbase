package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link SaveTaskRequestToTaskEntityMapper}.
 * This class verifies the correctness of the mapping logic from {@link SaveTaskRequest}
 * to {@link TaskEntity}.
 */
class SaveTaskRequestToTaskEntityMapperTest {

    private final SaveTaskRequestToTaskEntityMapper mapper = SaveTaskRequestToTaskEntityMapper.initialize();

    @Test
    void testMapSaveTaskRequestNull() {

        TaskEntity result = mapper.map((SaveTaskRequest) null);

        assertNull(result);

    }

    @Test
    void testMapSaveTaskRequestCollectionNull() {

        List<TaskEntity> result = mapper.map((Collection<SaveTaskRequest>) null);

        assertNull(result);

    }

    @Test
    void testMapSaveTaskRequestListEmpty() {

        List<TaskEntity> result = mapper.map(Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void testMapSaveTaskRequestListWithNullElements() {

        List<SaveTaskRequest> requests = Arrays.asList(new SaveTaskRequest("Task 1"), null);

        List<TaskEntity> result = mapper.map(requests);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertNull(result.get(1));

    }

    @Test
    void testMapSingleSaveTaskRequest() {

        SaveTaskRequest request = new SaveTaskRequest("Sample Task");

        TaskEntity result = mapper.map(request);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());

    }

    @Test
    void testMapSaveTaskRequestListWithValues() {

        SaveTaskRequest request1 = new SaveTaskRequest("Task 1");
        SaveTaskRequest request2 = new SaveTaskRequest("Task 2");

        List<SaveTaskRequest> requests = Arrays.asList(request1, request2);

        List<TaskEntity> result = mapper.map(requests);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(request1.getName(), result.get(0).getName());
        assertEquals(request2.getName(), result.get(1).getName());

    }

    @Test
    void testMapForSaving() {

        SaveTaskRequest request = new SaveTaskRequest("Saving Task");

        TaskEntity result = mapper.mapForSaving(request);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());

    }

    @Test
    void testMapWithEdgeCaseValues() {

        SaveTaskRequest request = new SaveTaskRequest("");

        TaskEntity result = mapper.map(request);

        assertNotNull(result);
        assertEquals("", result.getName());

    }

}