package com.example.todowithcouchbase.task.model.mapper;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.response.CustomPagingResponse;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CustomPageTaskToCustomPagingTaskResponseMapper {

    TaskToTaskResponseMapper taskToTaskResponseMapper = Mappers.getMapper(TaskToTaskResponseMapper.class);

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

    default List<TaskResponse> toTaskResponseList(List<Task> tasks) {

        if (tasks == null) {
            return null;
        }

        return tasks.stream()
                .map(taskToTaskResponseMapper::map)
                .collect(Collectors.toList());

    }

    static CustomPageTaskToCustomPagingTaskResponseMapper initialize() {
        return Mappers.getMapper(CustomPageTaskToCustomPagingTaskResponseMapper.class);
    }

}
