package com.example.todowithcouchbase.auth.service;

import com.example.todowithcouchbase.auth.model.dto.request.TokenInvalidateRequest;

public interface LogoutService {

    void logout(final TokenInvalidateRequest tokenInvalidateRequest);

}
