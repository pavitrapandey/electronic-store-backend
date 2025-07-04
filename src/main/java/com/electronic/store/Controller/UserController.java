package com.electronic.store.Controller;


import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.ImageResponse;
import com.electronic.store.dtos.PageableRespond;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.service.FileService;
import com.electronic.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "REST APIs for User Management!!!")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //create user
    @PostMapping
    @Operation(method = "createUser", summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    })
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //update user
    @PutMapping("/{userId}")
    @Operation(method = "updateUser", summary = "Update an existing user")
    public ResponseEntity<UserDto> updateUser(@Valid  @PathVariable("userId") String userId, @RequestBody UserDto userDto){
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    //delete user
    @DeleteMapping("/{userId}")
    @Operation(method = "deleteUser", summary = "Delete a user by ID")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId) throws IOException{
        userService.deleteUser(userId);
        ApiResponseMessage responseMessage= ApiResponseMessage.builder()
                .message("User deleted successfully ")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }


    //get user by id
    @GetMapping("/{userId}")
    @Operation(method = "getUserById", summary = "Get a user by ID")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId){
        UserDto user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //get all users
    @GetMapping
    @Operation(method = "getAllUsers", summary = "Get all users with pagination and sorting")
    public ResponseEntity<PageableRespond<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "size",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        return new ResponseEntity<>(userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir), HttpStatus.OK);
    }

//get user by email
    @GetMapping("/email/{email}")
    @Operation(method = "getUserByEmail", summary = "Get a user by email")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        UserDto user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

//search user
    @GetMapping("/search/{keyword}")
    @Operation(method = "searchUser", summary = "Search users by keyword")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword){
        List<UserDto> users = userService.searchUser(keyword);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //Upload user image
    @PostMapping("/image/{userId}")
    @Operation(method = "uploadUserImage", summary = "Upload an image for a user")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @PathVariable String userId,
            @RequestParam("image") MultipartFile image) throws IOException {

     String imageName=  fileService.uploadImage(image,imageUploadPath);
     UserDto user= userService.getUserById(userId);
     user.setImageName(imageName);
       UserDto updated= userService.updateUser(userId,user);

   ImageResponse imageResponse=  ImageResponse.builder().imageName(imageName).success(true).message("Image Uploaded").status(HttpStatus.CREATED).build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    //serve image
    @GetMapping("/image/{userId}")
    @Operation(method = "serveUserImage", summary = "Serve user image by user ID")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getUserById(userId);
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
