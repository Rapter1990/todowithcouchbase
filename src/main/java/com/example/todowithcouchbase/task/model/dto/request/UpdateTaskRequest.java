package com.example.todowithcouchbase.task.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request class used to update an existing task.
 * Contains a single field 'name' which represents the name of the task to be updated.
 * The name field cannot be empty.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateTaskRequest {

    @NotBlank(message = "name field cannot be empty")
    private String name;

}
