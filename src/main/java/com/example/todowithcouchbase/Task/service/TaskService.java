package com.example.todowithcouchbase.Task.service;

import com.example.todowithcouchbase.Task.model.dto.request.SaveTaskRequest;
import com.example.todowithcouchbase.Task.model.dto.response.SaveTaskResponse;
import com.example.todowithcouchbase.Task.model.entity.TaskEntity;

public interface TaskService {

    SaveTaskResponse saveTaskToDatabase(SaveTaskRequest taskRequest);

}
