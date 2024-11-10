package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.common.model.mapper.BaseMapper;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskToTaskResponseMapper extends BaseMapper<Task, TaskResponse> {

    static TaskToTaskResponseMapper initialize() {
        return Mappers.getMapper(TaskToTaskResponseMapper.class);
    }
}