package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.common.model.mapper.BaseMapper;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting a {@link TaskEntity} to a {@link Task}.
 * This interface extends the {@link BaseMapper} interface, enabling automatic mapping
 * between {@link TaskEntity} and {@link Task} objects.
 */
@Mapper
public interface TaskEntityToTaskMapper extends BaseMapper<TaskEntity, Task> {

    /**
     * Initializes and returns an instance of the {@link TaskEntityToTaskMapper}.
     *
     * @return an instance of the mapper
     */
    static TaskEntityToTaskMapper initialize() {
        return Mappers.getMapper(TaskEntityToTaskMapper.class);
    }

}
