package com.electronic.store.Controller;

import com.electronic.store.Security.JwtHelper;
import com.electronic.store.dtos.*;
import com.electronic.store.entities.User;
import com.electronic.store.service.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {


    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private RefreshTokenService refreshTokenService;

   private Logger logger= org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);

   @Autowired
   private UserDetailsService userDetailsService;

   //regenerate token
    @PostMapping("/regenerate-token")
    public ResponseEntity<JwtResponse> regenerateToken(@RequestBody RefreshTokenRequest request)
    {
        RefreshTokenDto refreshTokenDto = refreshTokenService.findByToken(request.getRefreshToken());
        RefreshTokenDto refreshTokenDto1=refreshTokenService.verifyToken(refreshTokenDto);
        UserDto user= refreshTokenService.getUser(refreshTokenDto1);
        String jwtToken = helper.generateToken(mapper.map(user, User.class));
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(jwtToken)
                .user(user)
                .refreshToken(refreshTokenDto1)
                .build();
        return ResponseEntity.ok(jwtResponse);
    }
    //login
    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
    this.doAuthenticate(request.getEmail(),request.getPassword());

    User user=(User) userDetailsService.loadUserByUsername(request.getEmail());
    String  token=helper.generateToken(user);

    //Refresh token
        RefreshTokenDto refreshTokenDto = refreshTokenService.createRefreshToken(user.getEmail());

    JwtResponse jwtResponse=JwtResponse.builder().
            token(token).
            user(mapper.map(user, UserDto.class)).
            refreshToken(refreshTokenDto).
            build();

        return ResponseEntity.ok(jwtResponse);
    }

    private void doAuthenticate(String email, String password) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            manager.authenticate(authentication);
        } catch (Exception e) {
            logger.error("Invalid username or password");
            throw new RuntimeException("Invalid username or password");
        }
    }
}
