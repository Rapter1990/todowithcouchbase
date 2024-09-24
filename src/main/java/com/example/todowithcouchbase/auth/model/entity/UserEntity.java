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

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserEntity extends BaseEntity {

    @Id
    @Field(name = "ID")
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
