package com.example.todowithcouchbase.auth.service.impl;

import com.example.todowithcouchbase.auth.exception.UserNotFoundException;
import com.example.todowithcouchbase.auth.exception.UserStatusNotValidException;
import com.example.todowithcouchbase.auth.model.Token;
import com.example.todowithcouchbase.auth.model.dto.request.TokenRefreshRequest;
import com.example.todowithcouchbase.auth.model.entity.UserEntity;
import com.example.todowithcouchbase.auth.model.enums.TokenClaims;
import com.example.todowithcouchbase.auth.model.enums.UserStatus;
import com.example.todowithcouchbase.auth.repository.UserRepository;
import com.example.todowithcouchbase.auth.service.RefreshTokenService;
import com.example.todowithcouchbase.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Override
    public Token refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        tokenService.verifyAndValidate(tokenRefreshRequest.getRefreshToken());

        final String adminId = tokenService
                .getPayload(tokenRefreshRequest.getRefreshToken())
                .get(TokenClaims.USER_ID.getValue())
                .toString();

        final UserEntity userEntityFromDB = userRepository
                .findById(adminId)
                .orElseThrow(UserNotFoundException::new);

        this.validateAdminStatus(userEntityFromDB);

        return tokenService.generateToken(
                userEntityFromDB.getClaims(),
                tokenRefreshRequest.getRefreshToken()
        );
    }

    private void validateAdminStatus(final UserEntity userEntity) {
        if (!(UserStatus.ACTIVE.equals(userEntity.getUserStatus()))) {
            throw new UserStatusNotValidException("UserStatus = " + userEntity.getUserStatus());
        }
    }

}
