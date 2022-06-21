package com.example.demospringsecurity.controller;

import com.example.demospringsecurity.dto.UserDto;
import com.example.demospringsecurity.model.MessageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface UserApi {

    @ApiOperation(value = "Find all users",
            notes = "Provide page for offset and size for limit because this is pageable",
            response = Page.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Page<UserDto> getUsers(@ApiParam(value = "Page value for offset") int page,
                           @ApiParam(value = "Size value for limit") int size);

    @ApiOperation(value = "Find user by username",
            notes = "Provide username to look up for specific user",
            response = UserDto.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto getUser(@ApiParam(value = "User username") String username);

    @ApiOperation(value = "Add authority for specific user",
            notes = "Provide username and authority",
            response = ResponseEntity.class,
            code = 201,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MessageResponse> addAuthorityToUser(@ApiParam(value = "User username") String username,
                                                       @ApiParam(value = "Authority or Role") String authority);

    @ApiOperation(value = "Add user",
            notes = "Provide user dto for request body",
            response = ResponseEntity.class,
            code = 201,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MessageResponse> saveUser(UserDto user);

    @ApiOperation(value = "Update user",
            notes = "Provide username and user dto for request body",
            response = ResponseEntity.class,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MessageResponse> updateUser(@ApiParam(value = "User username") String username, UserDto user);

    @ApiOperation(value = "Delete user",
            notes = "Provide username",
            response = ResponseEntity.class,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MessageResponse> deleteUser(@ApiParam(value = "User username") String username);
}