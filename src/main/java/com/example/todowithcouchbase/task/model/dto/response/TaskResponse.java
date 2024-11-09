package com.example.todowithcouchbase.task.model.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskResponse {

    private String id;

    private String name;

}
