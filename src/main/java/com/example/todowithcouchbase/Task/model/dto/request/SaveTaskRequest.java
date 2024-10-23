package com.example.todowithcouchbase.Task.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SaveTaskRequest {

    @NotBlank(message = "name field cannot be empty")
    private String name;

}
