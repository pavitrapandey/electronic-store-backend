package com.electronic.store.service.impl;

import com.electronic.store.Repository.UserRepository;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {

       String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);

        User user = dtoToEntity(userDto);
        // Here you would typically save the user to the database
       User user1= userRepository.save(user);
        // Convert the saved user entity back to DTO
        UserDto savedUserDto = entityToUserDto(user1);
        return savedUserDto;
    }

    @Override
    public UserDto getUserById(String userId) {
        // Find the user by ID
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        // Convert the user entity to DTO
        UserDto userDto = entityToUserDto(user);
        // Return the user DTO
        return userDto;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {

        // Find the user by ID
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        // Update the user details
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());
        user.setGender(userDto.getGender());
        // Save the updated user
        User updatedUser = userRepository.save(user);
        // Convert the updated user entity back to DTO
        UserDto updatedUserDto = entityToUserDto(updatedUser);
        // Return the updated user DTO

        return updatedUserDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        // Fetch all users from the repository
        List<User> users = userRepository.findAll();
        // Convert the list of user entities to a list of user DTOs
        List<UserDto> userDtos = users.stream()
                .map(user -> entityToUserDto(user))
                .collect(Collectors.toList());
        // Return the list of user DTOs
        return userDtos;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        // Find the user by email
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User with email "+email+" not found"));
        // Convert the user entity to DTO
        UserDto userDto = entityToUserDto(user);
        // Return the user DTO
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String Keyword) {
        // Search for users by keyword
        List<User> users = userRepository.findByKeyword(Keyword);
        // Convert the list of user entities to a list of user DTOs
        List<UserDto> userDtos = users.stream()
                .map(user -> entityToUserDto(user))
                .collect(Collectors.toList());
        return userDtos;
    }

    private UserDto entityToUserDto(User savedUser){
        UserDto userDto= UserDto.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .about(savedUser.getAbout())
                .imageName(savedUser.getImageName())
                .gender(savedUser.getGender())
                .build();
        return userDto;

    }

    private User dtoToEntity(UserDto userDto){
        User user=User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .about(userDto.getAbout())
                .imageName(userDto.getImageName())
                .gender(userDto.getGender())
                .build();
        return user;
    }
}
