package com.electronic.store.serviceImpl;

import com.electronic.store.Exception.EmailAlreadyExistException;
import com.electronic.store.Exception.ResourceNotFoundException;
import com.electronic.store.Repository.RoleRepository;
import com.electronic.store.Repository.UserRepository;
import com.electronic.store.dtos.PageableRespond;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Roles;
import com.electronic.store.entities.User;
import com.electronic.store.helper.Helper;
import com.electronic.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${user.profile.image.path}")
   private String filePath;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    // Create a new user
    @Override
    public UserDto createUser(UserDto userDto) {

       String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);

        // Check if the Email already exists
        Optional<User> existingEmail = userRepository.findByEmail(userDto.getEmail());
        if (existingEmail.isPresent()) {
            throw new EmailAlreadyExistException("Email id "+userDto.getEmail()+" already exists");
        }

        // Convert UserDto to User entity
        User user = dtoToEntity(userDto);
        // Set the password and encode it
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //assign the Normal role to the user
        Roles role=new Roles();
        role.setRoleId(UUID.randomUUID().toString());
        role.setName("ROLE_NORMAL");

        Roles normal= roleRepository.findByName("ROLE_NORMAL").orElse(role);
        user.setRoles(List.of(normal));

        // Here you would typically save the user to the database
       User user1= userRepository.save(user);
        // Convert the saved user entity back to DTO
        UserDto savedUserDto = entityToUserDto(user1);
        return savedUserDto;
    }

    // Get user by ID
    @Override
    public UserDto getUserById(String userId) {
        // Find the user by ID
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        // Convert the user entity to DTO
        UserDto userDto = entityToUserDto(user);
        // Return the user DTO
        return userDto;
    }

    // Update user details
    @Override
    public UserDto updateUser(String userId, UserDto userDto) {

        // Find the user by ID
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        // Update the user details
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());
        user.setGender(userDto.getGender());
        // Save the updated user
        User updatedUser = userRepository.save(user);
        // Convert the updated user entity back to DTO
        // Return the updated user DTO

        return entityToUserDto(updatedUser);
    }


    // Delete user by ID
    @Override
    public void deleteUser(String userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        String imageName=user.getImageName();
        String fullPath=filePath+imageName;

        Path path=Path.of(fullPath);
        try {
            Files.delete(path);
        } catch (NoSuchFileException e) {
            e.printStackTrace();
        }


        userRepository.delete(user);
    }

    // Get all users
    @Override
    public PageableRespond<UserDto> getAllUsers(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir
    )
    {
        // Create a sort object based on the provided sort direction
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending()) ;
        Pageable pageable= PageRequest.of(pageNumber, pageSize, sort);

        // Create a pageable object with the specified page number and size
        Page<User> page = userRepository.findAll(pageable);

        // Fetch all users from the repository
        List<User> users= page.getContent();

        // Convert the list of user entities to a list of user DTOs
        List<UserDto> userDtos = users.stream()
                .map(user -> entityToUserDto(user))
                .collect(Collectors.toList());

        // Create a pageable response object
        PageableRespond<UserDto> respond= Helper.getPageableResponse(page,UserDto.class);

        // Return the pageable response
        return respond;
    }

    // Get user by email
    @Override
    public UserDto getUserByEmail(String email)
    {
        // Find the user by email
        User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User with email "+email+" not found"));
        // Convert the user entity to DTO
        UserDto userDto = entityToUserDto(user);
        // Return the user DTO
        return userDto;
    }

    // Search for users by keyword
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


    // Helper methods to convert between User and UserDto
    private UserDto entityToUserDto(User savedUser){

        return modelMapper.map(savedUser,UserDto.class);

    }

    // Convert UserDto to User entity
    private User dtoToEntity(UserDto userDto){

        return modelMapper.map(userDto,User.class);
    }
}
