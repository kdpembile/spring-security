package com.example.demospringsecurity.controller;

import com.example.demospringsecurity.dto.UserDto;
import com.example.demospringsecurity.model.MessageResponse;
import com.example.demospringsecurity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    public static final String USER_NOT_FOUND = "User %s not found in the database";

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<UserDto> getUsers(@RequestParam int page, @RequestParam int size) {
        try {
            return userService.getUsers(page, size);

        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , e.getMessage(), e);
        }
    }

    @GetMapping(path = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUser(@PathVariable String username) {
        try {
            return userService.getUser(username);

        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , String.format(USER_NOT_FOUND, username), e);
        }
    }

    @PostMapping(path = "/user/authority/{username}/{authority}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> addAuthorityToUser(@PathVariable String username, @PathVariable String authority) {
        try {
            userService.addAuthorityToUser(username, authority);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse("Authority was successfully add to user"));

        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , String.format(USER_NOT_FOUND, username), e);

        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , "Failed to save authority to user", e);
        }
    }

    @PostMapping(path = "/user", consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> saveUser(@RequestBody UserDto user) {
        try {
            userService.saveUser(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse("User was successfully saved"));

        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , String.format("Failed to save user %s", user.getUsername()), e);
        }
    }

    @PutMapping(path = "/user/{username}", consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> updateUser(@PathVariable String username, @RequestBody UserDto user) {
        try {
            userService.updateUser(username, user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse("User was successfully updated"));

        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , String.format(USER_NOT_FOUND, username), e);

        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , "Failed to update user", e);
        }
    }

    @DeleteMapping(path = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse("User was successfully deleted"));

        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , String.format(USER_NOT_FOUND, username), e);

        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , "Failed to delete user", e);
        }
    }
}
