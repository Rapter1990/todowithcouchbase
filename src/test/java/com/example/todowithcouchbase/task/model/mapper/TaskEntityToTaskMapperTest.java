package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskEntityToTaskMapperTest {

    private final TaskEntityToTaskMapper mapper = TaskEntityToTaskMapper.initialize();

    @Test
    void testMapTaskEntityNull() {
        // Test null single TaskEntity
        Task result = mapper.map((TaskEntity) null);
        assertNull(result);
    }

    @Test
    void testMapTaskEntityCollectionNull() {

        List<Task> result = mapper.map((Collection<TaskEntity>) null);

        assertNull(result);

    }

    @Test
    void testMapTaskEntityListEmpty() {

        List<Task> result = mapper.map(Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void testMapTaskEntityListWithNullElements() {

        List<TaskEntity> taskEntities = Arrays.asList(new TaskEntity(), null);
        List<Task> result = mapper.map(taskEntities);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertNull(result.get(1));

    }

    @Test
    void testMapSingleTaskEntity() {

        TaskEntity taskEntity = TaskEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("Sample Task")
                .createdAt(LocalDateTime.now())
                .createdBy("user1")
                .updatedAt(LocalDateTime.now())
                .updatedBy("user2")
                .build();

        Task result = mapper.map(taskEntity);

        assertNotNull(result);
        assertEquals(taskEntity.getId(), result.getId());
        assertEquals(taskEntity.getName(), result.getName());
        assertEquals(taskEntity.getCreatedAt(), result.getCreatedAt());
        assertEquals(taskEntity.getCreatedBy(), result.getCreatedBy());
        assertEquals(taskEntity.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(taskEntity.getUpdatedBy(), result.getUpdatedBy());

    }

    @Test
    void testMapTaskEntityListWithValues() {

        TaskEntity taskEntity1 = TaskEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("Task 1")
                .build();

        TaskEntity taskEntity2 = TaskEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("Task 2")
                .build();

        List<TaskEntity> taskEntities = Arrays.asList(taskEntity1, taskEntity2);
        List<Task> result = mapper.map(taskEntities);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(taskEntity1.getId(), result.get(0).getId());
        assertEquals(taskEntity2.getId(), result.get(1).getId());

    }

    @Test
    void testMapWithEdgeCaseValues() {

        TaskEntity taskEntity = TaskEntity.builder()
                .name("")
                .createdAt(null)
                .createdBy(null)
                .updatedAt(null)
                .updatedBy(null)
                .build();

        Task result = mapper.map(taskEntity);

        assertNotNull(result);
        assertEquals("", result.getName());
        assertNull(result.getCreatedAt());
        assertNull(result.getCreatedBy());
        assertNull(result.getUpdatedAt());
        assertNull(result.getUpdatedBy());

    }

}