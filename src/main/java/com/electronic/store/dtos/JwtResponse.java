package com.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    UserDto user;
//    private String jwtToken;

    private RefreshTokenDto refreshToken;

}
