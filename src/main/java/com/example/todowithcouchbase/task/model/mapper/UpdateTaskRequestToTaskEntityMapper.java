package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.common.model.mapper.BaseMapper;
import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UpdateTaskRequestToTaskEntityMapper extends BaseMapper<UpdateTaskRequest, TaskEntity> {

    @Named("mapForUpdate")
    default void updateTaskMapper(final TaskEntity taskEntity, final UpdateTaskRequest updateTaskRequest){

        taskEntity.setName(updateTaskRequest.getName());

    }

    static UpdateTaskRequestToTaskEntityMapper initialize(){
        return Mappers.getMapper(UpdateTaskRequestToTaskEntityMapper.class);
    }

}
