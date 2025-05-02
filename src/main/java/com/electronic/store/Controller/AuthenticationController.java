package com.electronic.store.Controller;

import com.electronic.store.Security.JwtHelper;
import com.electronic.store.dtos.JwtRequest;
import com.electronic.store.dtos.JwtResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
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

   private Logger logger= org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);

   @Autowired
   private UserDetailsService userDetailsService;
    //login
    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
    this.doAuthenticate(request.getEmail(),request.getPassword());

    User user=(User) userDetailsService.loadUserByUsername(request.getEmail());
    String  token=helper.generateToken(user);

    JwtResponse jwtResponse=JwtResponse.builder().
            token(token).
            user(mapper.map(user, UserDto.class)).

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
