package com.example.todowithcouchbase.task.model.dto.response;

import lombok.*;

/**
 * Response class representing a task.
 * This class is used to send information about a task, including its ID and name.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskResponse {

    private String id;

    private String name;

}
