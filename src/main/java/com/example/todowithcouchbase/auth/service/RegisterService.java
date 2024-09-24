package com.example.todowithcouchbase.auth.service;

import com.example.todowithcouchbase.auth.model.User;
import com.example.todowithcouchbase.auth.model.dto.request.RegisterRequest;

public interface RegisterService {

    User registerUser(final RegisterRequest registerRequest);

}
