package com.example.todowithcouchbase.logging.service;

import com.example.todowithcouchbase.logging.entity.LogEntity;

/**
 * Service interface for handling log-related operations.
 * This interface defines a method for saving log entries to the database.
 * It abstracts the logic for logging and allows interaction with a persistence layer to store log data.
 */
public interface LogService {

    /**
     * Saves the provided {@link LogEntity} to the database.
     *
     * @param logEntity the {@link LogEntity} to be saved
     */
    void saveLogToDatabase(LogEntity logEntity);

}
