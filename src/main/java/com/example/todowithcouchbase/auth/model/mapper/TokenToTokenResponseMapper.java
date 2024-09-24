package com.example.todowithcouchbase.auth.model.mapper;

import com.example.todowithcouchbase.auth.model.Token;
import com.example.todowithcouchbase.auth.model.dto.response.TokenResponse;
import com.example.todowithcouchbase.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenToTokenResponseMapper extends BaseMapper<Token, TokenResponse> {

    @Override
    TokenResponse map(Token source);

    static TokenToTokenResponseMapper initialize() {
        return Mappers.getMapper(TokenToTokenResponseMapper.class);
    }

}
