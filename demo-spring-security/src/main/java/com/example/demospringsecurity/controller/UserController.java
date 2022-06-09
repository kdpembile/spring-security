package com.example.demospringsecurity.controller;

import com.example.demospringsecurity.dto.UserDto;
import com.example.demospringsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<UserDto> getUsers(@RequestParam int page, @RequestParam int size) {
        return userService.getUsers(page, size);
    }
}
