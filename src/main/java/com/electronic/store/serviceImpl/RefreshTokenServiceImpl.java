package com.electronic.store.serviceImpl;

import com.electronic.store.Exception.ResourceNotFoundException;
import com.electronic.store.Repository.RefreshTokenRepository;
import com.electronic.store.Repository.UserRepository;
import com.electronic.store.dtos.RefreshTokenDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.RefreshToken;
import com.electronic.store.entities.User;
import com.electronic.store.service.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RefreshTokenDto createRefreshToken(String username){
        // Fetch the user by username
        User user= userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        //check if token already exists for the user
      RefreshToken refreshToken=  refreshTokenRepository.findByUser(user).orElse(null);

        if(refreshToken==null){
            refreshToken=RefreshToken.builder()
                    .user(user)
                    .token(java.util.UUID.randomUUID().toString()) // Generate a random token
                    .expiryDate(Instant.now().plusSeconds(5*24*60*60))
                    .build();
        }else {
            // If a token already exists, update the existing token
            refreshToken.setToken(java.util.UUID.randomUUID().toString()); //Generate a new token
            refreshToken.setExpiryDate(Instant.now().plusSeconds(5*24*60*60)); // Extend the expiry date
        }


        // Save the refresh token to the repository
       RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

        return this.modelMapper.map(savedToken, RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto findByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found with token: " + token));
        return modelMapper.map(refreshToken, RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto verifyToken(RefreshTokenDto token){
        var RefreshToken = modelMapper.map(token, RefreshToken.class);

        if (token.getExpiryDate().compareTo(Instant.now())<0) {
            refreshTokenRepository.delete(RefreshToken);
            throw new ResourceNotFoundException("Token has expired");
        }
        return token;
    }

    @Override
    public UserDto getUser(RefreshTokenDto token)
    {
       RefreshToken refreshToken= refreshTokenRepository.findByToken(token.getToken()).orElseThrow(() -> new ResourceNotFoundException("Refresh token not found with token: " + token.getToken()));
        User user = refreshToken.getUser();
        return modelMapper.map(user, UserDto.class);
    }


}
