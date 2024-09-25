package com.example.todowithcouchbase.logging.repository;

import com.example.todowithcouchbase.logging.entity.LogEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface LogRepository extends CouchbaseRepository<LogEntity,String> {

}
