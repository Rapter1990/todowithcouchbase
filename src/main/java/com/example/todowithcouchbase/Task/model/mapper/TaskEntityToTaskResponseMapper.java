package com.example.todowithcouchbase.Task.model.mapper;

import com.example.todowithcouchbase.Task.model.dto.response.SaveTaskResponse;
import com.example.todowithcouchbase.Task.model.entity.TaskEntity;
import com.example.todowithcouchbase.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskEntityToTaskResponseMapper extends BaseMapper<TaskEntity, SaveTaskResponse> {


    static TaskEntityToTaskResponseMapper initialize() {
        return Mappers.getMapper(TaskEntityToTaskResponseMapper.class);
    }
}
