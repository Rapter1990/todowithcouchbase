package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.common.model.mapper.BaseMapper;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting a {@link Task} to a {@link TaskResponse}.
 * This interface extends the {@link BaseMapper} interface, enabling automatic mapping
 * between {@link Task} and {@link TaskResponse} objects.
 */
@Mapper
public interface TaskToTaskResponseMapper extends BaseMapper<Task, TaskResponse> {

    /**
     * Initializes and returns an instance of the {@link TaskToTaskResponseMapper}.
     * This method is used to obtain a mapper instance for converting a {@link Task}
     * object into a {@link TaskResponse} object.
     *
     * @return an instance of the {@link TaskToTaskResponseMapper} mapper
     */
    static TaskToTaskResponseMapper initialize() {
        return Mappers.getMapper(TaskToTaskResponseMapper.class);
    }

}