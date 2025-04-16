package com.electronic.store.service;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;

import java.util.List;

/**
 * @author Pavitra
 * @project electronic-store
 * @created 15/04/2025
 */
public interface UserService {

    UserDto createUser(UserDto userDto);
    UserDto getUserById(String userId);
    UserDto updateUser(String userId, UserDto userDto);
    void deleteUser(String userId);
    List<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
    UserDto getUserByEmail(String email);
    List<UserDto> searchUser(String Keyword);


}
