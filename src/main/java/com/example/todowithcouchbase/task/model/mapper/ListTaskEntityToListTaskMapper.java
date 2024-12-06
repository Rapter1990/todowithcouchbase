package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper interface that converts a list of {@link TaskEntity} objects to a list of {@link Task} objects.
 * It leverages MapStruct for automatic mapping between entity and domain objects.
 */
@Mapper
public interface ListTaskEntityToListTaskMapper {

    TaskEntityToTaskMapper taskEntityToTaskMapper = Mappers.getMapper(TaskEntityToTaskMapper.class);

    /**
     * Converts a list of {@link TaskEntity} objects to a list of {@link Task} objects.
     *
     * @param taskEntities the list of {@link TaskEntity} objects to be converted
     * @return a list of {@link Task} objects, or {@code null} if {@code taskEntities} is {@code null}
     */
    default List<Task> toTaskList(List<TaskEntity> taskEntities) {

        if (taskEntities == null) {
            return null;
        }

        return taskEntities.stream()
                .map(taskEntityToTaskMapper::map)
                .collect(Collectors.toList());

    }

    /**
     * Initializes and returns an instance of the {@link ListTaskEntityToListTaskMapper}.
     *
     * @return an instance of the mapper
     */
    static ListTaskEntityToListTaskMapper initialize() {
        return Mappers.getMapper(ListTaskEntityToListTaskMapper.class);
    }

}
