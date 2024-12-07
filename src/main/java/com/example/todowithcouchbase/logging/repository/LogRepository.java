package com.example.todowithcouchbase.logging.repository;

import com.example.todowithcouchbase.logging.entity.LogEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

/**
 * Repository interface for {@link LogEntity}.
 * This interface extends {@link CouchbaseRepository} and provides methods for interacting with
 * the Couchbase database for storing and retrieving {@link LogEntity} instances.
 * The repository will automatically inherit basic CRUD operations such as save, find, and delete
 * from {@link CouchbaseRepository}.
 */
public interface LogRepository extends CouchbaseRepository<LogEntity,String> {

}
