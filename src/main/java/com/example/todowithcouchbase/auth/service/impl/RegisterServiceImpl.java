package com.example.todowithcouchbase.auth.service.impl;

import com.example.todowithcouchbase.auth.exception.UserAlreadyExistException;
import com.example.todowithcouchbase.auth.model.User;
import com.example.todowithcouchbase.auth.model.dto.request.RegisterRequest;
import com.example.todowithcouchbase.auth.model.entity.UserEntity;
import com.example.todowithcouchbase.auth.model.mapper.RegisterRequestToUserEntityMapper;
import com.example.todowithcouchbase.auth.model.mapper.UserEntityToUserMapper;
import com.example.todowithcouchbase.auth.repository.UserRepository;
import com.example.todowithcouchbase.auth.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;

    private final RegisterRequestToUserEntityMapper registerRequestToUserEntityMapper =
            RegisterRequestToUserEntityMapper.initialize();

    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();

    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterRequest registerRequest) {

        if (userRepository.existsUserEntityByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistException("The email is already used for another user : " + registerRequest.getEmail());
        }

        final UserEntity userEntityToBeSaved = registerRequestToUserEntityMapper.mapForSaving(registerRequest);

        userEntityToBeSaved.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        final UserEntity savedUserEntity = userRepository.save(userEntityToBeSaved);

        return userEntityToUserMapper.map(savedUserEntity);

    }

}
