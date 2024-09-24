package com.example.todowithcouchbase.auth.repository;

import com.example.todowithcouchbase.auth.model.entity.InvalidTokenEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;


public interface InvalidTokenRepository extends CouchbaseRepository<InvalidTokenEntity, String> {

    Optional<InvalidTokenEntity> findByTokenId(final String tokenId);

}
