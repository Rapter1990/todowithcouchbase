package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ListTaskEntityToListTaskMapper {

    TaskEntityToTaskMapper taskEntityToTaskMapper = Mappers.getMapper(TaskEntityToTaskMapper.class);

    default List<Task> toProductList(List<TaskEntity> taskEntities) {

        if (taskEntities == null) {
            return null;
        }

        return taskEntities.stream()
                .map(taskEntityToTaskMapper::map)
                .collect(Collectors.toList());

    }

    static ListTaskEntityToListTaskMapper initialize() {
        return Mappers.getMapper(ListTaskEntityToListTaskMapper.class);
    }

}
