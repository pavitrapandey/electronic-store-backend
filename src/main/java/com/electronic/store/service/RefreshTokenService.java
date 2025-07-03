package com.electronic.store.service;

import com.electronic.store.dtos.RefreshTokenDto;
import com.electronic.store.dtos.UserDto;

public interface RefreshTokenService {
    //create
    RefreshTokenDto createRefreshToken(String username);

    // find by token

    RefreshTokenDto findByToken(String token);

    //verify token
    RefreshTokenDto verifyToken(RefreshTokenDto token);

    //get user
    UserDto getUser(RefreshTokenDto token);
}
