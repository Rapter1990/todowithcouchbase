package com.example.todowithcouchbase.Task.model.mapper;

import com.example.todowithcouchbase.Task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.Task.model.entity.TaskEntity;
import com.example.todowithcouchbase.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SaveTaskRequestToTaskEntityMapper extends BaseMapper<SaveTaskRequest, TaskEntity> {

    @Named("mapForSaving")
    default TaskEntity mapForSaving(SaveTaskRequest request){
        return TaskEntity.builder()
                .name(request.getName())
                .build();
    }

    static SaveTaskRequestToTaskEntityMapper initialize(){
        return Mappers.getMapper(SaveTaskRequestToTaskEntityMapper.class);
    }
}
