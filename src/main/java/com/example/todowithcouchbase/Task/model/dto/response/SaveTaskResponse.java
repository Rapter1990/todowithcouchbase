package com.example.todowithcouchbase.Task.model.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SaveTaskResponse {

    private String id;

    private String name;
}
