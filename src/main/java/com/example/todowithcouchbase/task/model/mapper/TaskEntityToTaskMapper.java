package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.common.model.mapper.BaseMapper;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskEntityToTaskMapper extends BaseMapper<TaskEntity, Task> {

    static TaskEntityToTaskMapper initialize() {
        return Mappers.getMapper(TaskEntityToTaskMapper.class);
    }

}
