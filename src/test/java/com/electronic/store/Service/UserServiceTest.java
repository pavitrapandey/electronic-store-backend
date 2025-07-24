package com.electronic.store.Service;

import com.electronic.store.Repository.RoleRepository;
import com.electronic.store.Repository.UserRepository;
import com.electronic.store.dtos.PageableRespond;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Roles;
import com.electronic.store.entities.User;
import com.electronic.store.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    User user;
    Roles role;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void init(){
        role=Roles.builder().roleId("ROLE_USER").name("User").build();
       user= User.builder()
                .name("Pavitra")
                .email("pavitra@gmail.com")
                .password("pavitra07")
                .gender("Male")
                .about("Software Developer")
                .imageName("pavitra.jpg").build();


    }


    public void createUserTest(){

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));

        UserDto savedUser=userService.createUser(modelMapper.map(user,UserDto.class));
        Assertions.assertNotNull(savedUser);
    }


    public void updateUserTest() {
        String userId = "pabdic";

        UserDto info = UserDto.builder().name("Pavitra Pandey")
                .email("pavitra77@gmail.com")
                .gender("Male")
                .password("pavitra07")
                .about("Software Engineer")
                .imageName("Krishna.jpg").build();

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updatedUser = userService.updateUser(userId, info);
        Assertions.assertNotNull(updatedUser);
    }


    public void deleteUserTest() throws IOException {
        String userId = "pabdic";
        Mockito.when(userRepository.findById("pabdic")).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        System.out.println("Delete the user");
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }


    public void getUserByIdTest(){
        String userId = "pabdic";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDto userDto = userService.getUserById(userId);
        System.out.println("User Name is "+userDto.getName());
        Assertions.assertNotNull(userDto);

    }

    @Test
    public void getAllUserTest(){

       User user1= User.builder()
                .name("Krishna")
                .email("krishna@gmail.com")
                .password("12345")
                .gender("Male")
                .about("Software Developer")
                .imageName("pitta.jpg").build();
       User user2= User.builder()
                .name("Sakshi")
                .email("krishna@gmail.com")
                .password("12345")
                .gender("Female")
                .about("Software Developer")
                .imageName("sakku.jpg").build();
        List<User> userList= Arrays.asList(user,user1,user2);
        Page<User> page= new PageImpl<>(userList);

        Mockito.when(userRepository.findAll((Pageable)Mockito.any( ))).thenReturn(page);
        PageableRespond<UserDto> respond= userService.getAllUsers(1,3,"name","asc");

        Assertions.assertEquals(3, respond.getContent().size());
    }
}
