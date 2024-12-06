package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.response.CustomPagingResponse;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper interface that converts a {@link CustomPage} of {@link Task} entities to a {@link CustomPagingResponse} of {@link TaskResponse}.
 * It leverages MapStruct for automatic mapping between domain and DTO objects.
 */
@Mapper
public interface CustomPageTaskToCustomPagingTaskResponseMapper {

    TaskToTaskResponseMapper taskToTaskResponseMapper = Mappers.getMapper(TaskToTaskResponseMapper.class);

    /**
     * Converts a {@link CustomPage} of {@link Task} entities to a {@link CustomPagingResponse} containing {@link TaskResponse} DTOs.
     *
     * @param taskPage the {@link CustomPage} containing a list of {@link Task} entities
     * @return a {@link CustomPagingResponse} with the mapped {@link TaskResponse} list, or {@code null} if {@code taskPage} is {@code null}
     */
    default CustomPagingResponse<TaskResponse> toPagingResponse(CustomPage<Task> taskPage) {

        if (taskPage == null) {
            return null;
        }

        return CustomPagingResponse.<TaskResponse>builder()
                .content(toTaskResponseList(taskPage.getContent()))
                .totalElementCount(taskPage.getTotalElementCount())
                .totalPageCount(taskPage.getTotalPageCount())
                .pageNumber(taskPage.getPageNumber())
                .pageSize(taskPage.getPageSize())
                .build();

    }

    /**
     * Converts a list of {@link Task} entities to a list of {@link TaskResponse} DTOs.
     *
     * @param tasks the list of {@link Task} entities
     * @return a list of {@link TaskResponse} DTOs, or {@code null} if {@code tasks} is {@code null}
     */
    default List<TaskResponse> toTaskResponseList(List<Task> tasks) {

        if (tasks == null) {
            return null;
        }

        return tasks.stream()
                .map(taskToTaskResponseMapper::map)
                .collect(Collectors.toList());

    }

    /**
     * Initializes and returns an instance of the {@link CustomPageTaskToCustomPagingTaskResponseMapper}.
     *
     * @return an instance of the mapper
     */
    static CustomPageTaskToCustomPagingTaskResponseMapper initialize() {
        return Mappers.getMapper(CustomPageTaskToCustomPagingTaskResponseMapper.class);
    }

}
