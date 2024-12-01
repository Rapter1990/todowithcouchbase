package com.example.todowithcouchbase.task.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateTaskRequest {

    @NotBlank(message = "name field cannot be empty")
    private String name;

}
