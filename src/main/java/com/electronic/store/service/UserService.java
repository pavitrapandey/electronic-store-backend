package com.electronic.store.service;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);
    UserDto getUserById(String userId);
    UserDto updateUser(String userId, UserDto userDto);
    void deleteUser(String userId);
    List<UserDto> getAllUsers();
    UserDto getUserByEmail(String email);
    List<UserDto> searchUser(String Keyword);
}
