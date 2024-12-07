package com.example.todowithcouchbase.task.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request class used to get a task by its name.
 * Contains a single field 'name' which represents the name of the task to be fetched.
 * The name field cannot be empty.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetTaskByNameRequest {

    @NotBlank(message = "name field cannot be empty")
    private String name;

}
