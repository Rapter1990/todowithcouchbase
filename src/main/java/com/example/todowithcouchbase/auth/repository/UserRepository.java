package com.example.todowithcouchbase.auth.repository;

import com.example.todowithcouchbase.auth.model.entity.UserEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;

public interface UserRepository extends CouchbaseRepository<UserEntity, String> {

    boolean existsUserEntityByEmail(final String email);

    Optional<UserEntity> findUserEntityByEmail(final String email);

}
