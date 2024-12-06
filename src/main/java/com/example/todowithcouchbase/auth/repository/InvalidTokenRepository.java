package com.example.todowithcouchbase.auth.repository;

import com.example.todowithcouchbase.auth.model.entity.InvalidTokenEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link InvalidTokenEntity} objects in Couchbase.
 * This interface extends {@link CouchbaseRepository} to provide CRUD operations for the {@link InvalidTokenEntity}.
 * It also includes custom query methods to interact with the data store.
 */
public interface InvalidTokenRepository extends CouchbaseRepository<InvalidTokenEntity, String> {

    /**
     * Finds an {@link InvalidTokenEntity} by its token ID.
     * This method is used to retrieve an invalid token entity based on the unique token ID.
     *
     * @param tokenId The unique ID of the token to search for.
     * @return An {@link Optional} containing the {@link InvalidTokenEntity} if found, or {@link Optional#empty()} if not found.
     */
    Optional<InvalidTokenEntity> findByTokenId(final String tokenId);

}
