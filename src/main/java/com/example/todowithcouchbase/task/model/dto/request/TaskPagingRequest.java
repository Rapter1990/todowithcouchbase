package com.example.todowithcouchbase.task.model.dto.request;

import com.example.todowithcouchbase.common.model.dto.request.CustomPagingRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Request class used for paginated task retrieval.
 * Inherits from the base class {@link CustomPagingRequest}, which provides pagination details such as page number and size.
 * This class is used to request a paginated list of tasks.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TaskPagingRequest extends CustomPagingRequest {

}
