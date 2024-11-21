package com.example.todowithcouchbase.task.model.mapper.task;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.response.CustomPagingResponse;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.response.TaskResponse;

public class ToPagingResponse {
    private final static TaskToTaskResponseMapper taskToTaskResponseMapper =
            TaskToTaskResponseMapper.initialize();

    public static CustomPagingResponse<TaskResponse> taskCustomPagingResponse(
            final CustomPage<Task> customPage
            ){
        return CustomPagingResponse.<TaskResponse>builder()
                .of(customPage)
                .content(
                        customPage.getContent() == null ? null:
                                customPage.getContent().stream().map(taskToTaskResponseMapper::map).toList()
                )
                .build();
    }
}
