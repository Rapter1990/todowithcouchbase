package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.response.TaskResponse;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link TaskToTaskResponseMapper}.
 * This class verifies the correctness of the mapping logic from Task objects to {@link TaskResponse}.
 */
class TaskToTaskResponseMapperTest {

    private final TaskToTaskResponseMapper mapper = TaskToTaskResponseMapper.initialize();

    @Test
    void testMapTaskNull() {

        TaskResponse result = mapper.map((Task) null);

        assertNull(result);

    }

    @Test
    void testMapTaskCollectionNull() {

        List<TaskResponse> result = mapper.map((Collection<Task>) null);

        assertNull(result);

    }

    @Test
    void testMapTaskListEmpty() {

        List<TaskResponse> result = mapper.map(Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void testMapTaskListWithNullElements() {

        List<Task> tasks = Arrays.asList(new Task(), null);

        List<TaskResponse> result = mapper.map(tasks);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertNull(result.get(1));

    }

    @Test
    void testMapSingleTask() {

        Task task = Task.builder()
                .id(UUID.randomUUID().toString())
                .name("Sample Task")
                .build();

        TaskResponse result = mapper.map(task);

        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getName(), result.getName());

    }

    @Test
    void testMapTaskListWithValues() {

        Task task1 = Task.builder()
                .id(UUID.randomUUID().toString())
                .name("Task 1")
                .build();

        Task task2 = Task.builder()
                .id(UUID.randomUUID().toString())
                .name("Task 2")
                .build();

        List<Task> tasks = Arrays.asList(task1, task2);
        List<TaskResponse> result = mapper.map(tasks);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(task1.getId(), result.get(0).getId());
        assertEquals(task2.getId(), result.get(1).getId());

    }

    @Test
    void testMapWithEdgeCaseValues() {

        Task task = Task.builder()
                .id("")
                .name("")
                .build();

        TaskResponse result = mapper.map(task);

        assertNotNull(result);
        assertEquals("", result.getId());
        assertEquals("", result.getName());

    }

}
