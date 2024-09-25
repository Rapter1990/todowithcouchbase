package com.example.todowithcouchbase.logging.service;

import com.example.todowithcouchbase.logging.entity.LogEntity;

public interface LogService {
    void saveLogToDatabase(LogEntity logEntity);
}
