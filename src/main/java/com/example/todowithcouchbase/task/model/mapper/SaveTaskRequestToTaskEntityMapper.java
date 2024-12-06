package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.common.model.mapper.BaseMapper;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting a {@link SaveTaskRequest} to a {@link TaskEntity}.
 * This interface extends the {@link BaseMapper} interface, allowing automatic mapping
 * between the {@link SaveTaskRequest} and {@link TaskEntity} objects.
 */
@Mapper
public interface SaveTaskRequestToTaskEntityMapper extends BaseMapper<SaveTaskRequest, TaskEntity> {

    /**
     * Converts a {@link SaveTaskRequest} to a {@link TaskEntity}.
     * This method is used for saving a task, mapping only the necessary fields from the
     * {@link SaveTaskRequest} to the {@link TaskEntity}.
     *
     * @param request the {@link SaveTaskRequest} to be mapped
     * @return the resulting {@link TaskEntity} containing the mapped fields
     */
    @Named("mapForSaving")
    default TaskEntity mapForSaving(SaveTaskRequest request){
        return TaskEntity.builder()
                .name(request.getName())
                .build();
    }

    /**
     * Initializes and returns an instance of the {@link SaveTaskRequestToTaskEntityMapper}.
     *
     * @return an instance of the mapper
     */
    static SaveTaskRequestToTaskEntityMapper initialize(){
        return Mappers.getMapper(SaveTaskRequestToTaskEntityMapper.class);
    }

}
