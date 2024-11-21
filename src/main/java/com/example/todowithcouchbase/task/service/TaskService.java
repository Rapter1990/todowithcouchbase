package com.example.todowithcouchbase.task.service;

import com.example.todowithcouchbase.common.model.CustomPage;
import com.example.todowithcouchbase.common.model.dto.request.CustomPagingRequest;
import com.example.todowithcouchbase.task.model.Task;
import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;



public interface TaskService {

    Task saveTaskToDatabase(SaveTaskRequest taskRequest);

    CustomPage<Task> getAllTasks(final CustomPagingRequest customPagingRequest);

}
