package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.common.model.mapper.BaseMapper;
import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting and updating a {@link TaskEntity} using data from an {@link UpdateTaskRequest}.
 * This interface extends the {@link BaseMapper} interface, enabling automatic mapping
 * between {@link UpdateTaskRequest} and {@link TaskEntity}.
 */
@Mapper
public interface UpdateTaskRequestToTaskEntityMapper extends BaseMapper<UpdateTaskRequest, TaskEntity> {

    /**
     * Updates the fields of an existing {@link TaskEntity} with data from an {@link UpdateTaskRequest}.
     * This method is used to map the fields from the request to the entity, specifically
     * updating the name of the task.
     *
     * @param taskEntity the {@link TaskEntity} to be updated
     * @param updateTaskRequest the {@link UpdateTaskRequest} containing the new data
     */
    @Named("mapForUpdate")
    default void updateTaskMapper(final TaskEntity taskEntity, final UpdateTaskRequest updateTaskRequest){

        taskEntity.setName(updateTaskRequest.getName());

    }

    /**
     * Initializes and returns an instance of the {@link UpdateTaskRequestToTaskEntityMapper}.
     * This method is used to obtain a mapper instance for mapping an {@link UpdateTaskRequest}
     * to a {@link TaskEntity}, including updating an existing entity.
     *
     * @return an instance of the {@link UpdateTaskRequestToTaskEntityMapper} mapper
     */
    static UpdateTaskRequestToTaskEntityMapper initialize(){
        return Mappers.getMapper(UpdateTaskRequestToTaskEntityMapper.class);
    }

}
