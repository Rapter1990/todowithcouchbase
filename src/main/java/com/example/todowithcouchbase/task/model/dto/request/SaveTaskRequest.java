package com.example.todowithcouchbase.task.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request class used to save a new task.
 * Contains a single field 'name' which represents the name of the task to be created.
 * The name field cannot be empty.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SaveTaskRequest {

    @NotBlank(message = "name field cannot be empty")
    private String name;

}
