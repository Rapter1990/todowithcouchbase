package com.example.todowithcouchbase.auth.model.entity;

import com.example.todowithcouchbase.auth.model.enums.TokenClaims;
import com.example.todowithcouchbase.auth.model.enums.UserStatus;
import com.example.todowithcouchbase.auth.model.enums.UserType;
import com.example.todowithcouchbase.common.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a user entity in the system.
 * This entity class stores user information, including personal details (e.g.,
 * first name, last name, email, etc.) and their status and type. It is
 * mapped to a MongoDB collection for storing user records. Each user has
 * an ID that is automatically generated.
 * This entity extends from {@link BaseEntity}, inheriting common fields
 * like created and updated timestamps.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document
@Scope("user-scope")
@Collection("user-collection")
public class UserEntity extends BaseEntity {

    @Id
    @Field(name = "ID")
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field(name = "EMAIL")
    private String email;

    @Field(name = "PASSWORD")
    private String password;

    @Field(name = "FIRST_NAME")
    private String firstName;

    @Field(name = "LAST_NAME")
    private String lastName;

    @Field(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Field(name = "USER_TYPE")
    private UserType userType;

    @Builder.Default
    @Field(name = "USER_STATUS")
    private UserStatus userStatus = UserStatus.ACTIVE;

    /**
     * Generates a map of claims associated with the user.
     * This method creates a map of key-value pairs representing the user's claims,
     * which can be used for JWT token generation or other purposes where user-related
     * data needs to be included.
     *
     * @return a map containing the user's claims.
     */
    public Map<String, Object> getClaims() {
        final Map<String, Object> claims = new HashMap<>();

        claims.put(TokenClaims.USER_ID.getValue(), this.id);
        claims.put(TokenClaims.USER_TYPE.getValue(), this.userType);
        claims.put(TokenClaims.USER_STATUS.getValue(), this.userStatus);
        claims.put(TokenClaims.USER_FIRST_NAME.getValue(), this.firstName);
        claims.put(TokenClaims.USER_LAST_NAME.getValue(), this.lastName);
        claims.put(TokenClaims.USER_EMAIL.getValue(), this.email);
        claims.put(TokenClaims.USER_PHONE_NUMBER.getValue(), this.phoneNumber);

        return claims;
    }

}
