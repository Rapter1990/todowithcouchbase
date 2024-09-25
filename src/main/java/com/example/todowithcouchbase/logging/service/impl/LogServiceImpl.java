package com.example.todowithcouchbase.logging.service.impl;

import com.example.todowithcouchbase.logging.entity.LogEntity;
import com.example.todowithcouchbase.logging.repository.LogRepository;
import com.example.todowithcouchbase.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    public void saveLogToDatabase(LogEntity logEntity) {
        logEntity.setTime(LocalDateTime.now());
        logRepository.save(logEntity);
    }

}
