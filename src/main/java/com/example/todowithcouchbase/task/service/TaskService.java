package com.example.todowithcouchbase.task.service;

import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.task.model.dto.response.SaveTaskResponse;

public interface TaskService {

    SaveTaskResponse saveTaskToDatabase(SaveTaskRequest taskRequest);

}
