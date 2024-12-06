package com.example.todowithcouchbase.common.config;

import com.example.todowithcouchbase.auth.model.enums.TokenClaims;
import com.example.todowithcouchbase.common.model.entity.BaseEntity;
import org.springframework.context.event.EventListener;
import org.springframework.data.couchbase.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.couchbase.core.mapping.event.BeforeSaveEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This class listens for Couchbase entity events and automatically updates entity fields
 * such as `createdAt`, `createdBy`, `updatedAt`, and `updatedBy` before saving or converting entities.
 */
@Component
public class CouchbaseEntityListener {

    /**
     * Event listener that is triggered before an entity is converted.
     * It sets the `createdAt` and `createdBy` fields if they are not already set.
     *
     * @param event the event triggered before the entity is converted
     * @param <T> the type of the entity that extends `BaseEntity`
     */
    @EventListener
    public <T extends BaseEntity> void onBeforeConvert(BeforeConvertEvent<T> event) {
        T entity = event.getSource();
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
            entity.setCreatedBy(getCurrentUser());
        }
    }

    /**
     * Event listener that is triggered before an entity is saved.
     * It sets the `updatedAt` and `updatedBy` fields with the current time
     * and the current user's email.
     *
     * @param event the event triggered before the entity is saved
     * @param <T> the type of the entity that extends `BaseEntity`
     */
    @EventListener
    public <T extends BaseEntity> void onBeforeSave(BeforeSaveEvent<T> event) {
        T entity = event.getSource();
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(getCurrentUser());
    }

    /**
     * Retrieves the email of the currently authenticated user from the security context.
     * If no authenticated user is found, it returns "anonymousUser".
     *
     * @return the email of the current user or "anonymousUser" if not authenticated
     */
    private String getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(user -> !"anonymousUser".equals(user))
                .map(Jwt.class::cast)
                .map(jwt -> jwt.getClaim(TokenClaims.USER_EMAIL.getValue()).toString())
                .orElse("anonymousUser");
    }

}
