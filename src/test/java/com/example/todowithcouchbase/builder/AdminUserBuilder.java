package com.example.todowithcouchbase.builder;

import com.example.todowithcouchbase.auth.model.entity.UserEntity;
import com.example.todowithcouchbase.auth.model.enums.UserStatus;
import com.example.todowithcouchbase.auth.model.enums.UserType;

import java.util.UUID;

public class AdminUserBuilder extends BaseBuilder<UserEntity> {


    public AdminUserBuilder() {
        super(UserEntity.class);
    }

    public AdminUserBuilder withValidFields() {
        return this
                .withId(UUID.randomUUID().toString())
                .withEmail("adminexample@example.com")
                .withPassword("adminpassword")
                .withFirstName("Admin First Name")
                .withLastName("Admin Last Name")
                .withPhoneNumber("1234567890")
                .withUserType(UserType.ADMIN)
                .withUserStatus(UserStatus.ACTIVE);
    }

    public AdminUserBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminUserBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public AdminUserBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public AdminUserBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AdminUserBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public AdminUserBuilder withPhoneNumber(String phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AdminUserBuilder withUserType(UserType userType) {
        data.setUserType(userType);
        return this;
    }

    public AdminUserBuilder withUserStatus(UserStatus userStatus) {
        data.setUserStatus(userStatus);
        return this;
    }

}
