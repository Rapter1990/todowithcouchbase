package com.example.todowithcouchbase.task.model.dto.request;

import com.example.todowithcouchbase.common.model.dto.request.CustomPagingRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TaskPagingRequest extends CustomPagingRequest {

}
