package com.electronic.store.Controller;

import com.electronic.store.Configuration.TestSecurityConfig;
import com.electronic.store.Repository.UserRepository;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Roles;
import com.electronic.store.entities.User;
import com.electronic.store.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class UserControllerTest{

    @MockitoBean
    private UserService userService;

    private User user;
    private Roles role;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void init(){
        user= User.builder()
                .name("Pavitra")
                .email("pavitra@gmail.com")
                .password("Secure@123")
                .gender("Male")
                .about("Software Developer")
                .imageName("pavitra.jpg")
                .build();
    }


    public void createUserTest() throws Exception {
        // Test logic for creating a user

        UserDto dto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        //Actual request fo URL
        this.mvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(convertObjectToJson(dto))
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());

    }

    private String convertObjectToJson(Object user){
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }


    public void updateUserTest() throws Exception {
        // Test logic for updating a user
        String userId = "pabdic";
        UserDto dto=modelMapper.map(user,UserDto.class);

        Mockito.when(userService.updateUser(Mockito.anyString(), Mockito.any())).thenReturn(dto);

        this.mvc.perform(
                MockMvcRequestBuilders.put("/api/users/" + userId)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(convertObjectToJson(dto))
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void getUserTest() throws Exception {
        // Test logic for getting a user by ID
        String userId = "pabdic";
        UserDto dto = modelMapper.map(user, UserDto.class);

        Mockito.when(userService.getUserById(Mockito.anyString())).thenReturn(dto);

        this.mvc.perform(
                MockMvcRequestBuilders.get("/api/users/" + userId)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }
}
