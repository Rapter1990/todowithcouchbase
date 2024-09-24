package com.example.todowithcouchbase.auth.service;

import com.example.todowithcouchbase.auth.model.Token;
import com.example.todowithcouchbase.auth.model.dto.request.TokenRefreshRequest;

public interface RefreshTokenService {

    Token refreshToken(final TokenRefreshRequest tokenRefreshRequest);

}
