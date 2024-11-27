package com.example.todowithcouchbase.task.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetTaskByNameRequest {

    @NotBlank(message = "name field cannot be empty")
    private String name;

}
