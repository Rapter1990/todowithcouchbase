package com.example.todowithcouchbase.task.service;

import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;

public interface TaskService {

    Task saveTaskToDatabase(SaveTaskRequest taskRequest);

}
