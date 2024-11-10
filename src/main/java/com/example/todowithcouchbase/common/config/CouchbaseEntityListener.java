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

@Component
public class CouchbaseEntityListener {

    @EventListener
    public <T extends BaseEntity> void onBeforeConvert(BeforeConvertEvent<T> event) {
        T entity = event.getSource();
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
            entity.setCreatedBy(getCurrentUser());
        }
    }

    @EventListener
    public <T extends BaseEntity> void onBeforeSave(BeforeSaveEvent<T> event) {
        T entity = event.getSource();
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(getCurrentUser());
    }

    private String getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(user -> !"anonymousUser".equals(user))
                .map(Jwt.class::cast)
                .map(jwt -> jwt.getClaim(TokenClaims.USER_EMAIL.getValue()).toString())
                .orElse("anonymousUser");
    }
}
