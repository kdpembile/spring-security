package com.example.demospringsecurity.service;

import com.example.demospringsecurity.dto.UserDto;
import com.example.demospringsecurity.entity.UserEntity;
import org.springframework.data.domain.Page;

public interface UserService {

    /**
     * Save new user
     *
     * @param userEntity user entity
     */
    void saveUser(UserEntity userEntity);

    /**
     * Return user dto
     *
     * @param username user username
     * @return UserDto class
     */
    UserDto getUser(String username);

    /**
     * Return list of UserDto object
     *
     * @param page offset
     * @param size limit
     * @return Page of UserDto class
     */
    Page<UserDto> getUsers(int page, int size);

    /**
     * Update user
     *
     * @param username user username
     * @param userDto user dto
     */
    void updateUser(String username, UserDto userDto);

    /**
     * Delete user
     *
     * @param username user username
     */
    void deleteUser(String username);
}
