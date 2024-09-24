package com.example.todowithcouchbase.auth.service;

import com.example.todowithcouchbase.auth.model.Token;
import com.example.todowithcouchbase.auth.model.dto.request.LoginRequest;

public interface LoginService {

    Token login(final LoginRequest loginRequest);

}
