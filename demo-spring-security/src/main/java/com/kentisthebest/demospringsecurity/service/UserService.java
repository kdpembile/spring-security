package com.kentisthebest.demospringsecurity.service;

import com.kentisthebest.demospringsecurity.dto.UserDto;
import org.springframework.data.domain.Page;

public interface UserService {

    /**
     * Save new user
     *
     * @param userDto user details
     */
    void saveUser(UserDto userDto);

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
     * Add authority to user
     *
     * @param username  user username
     * @param role user role
     */
    void addAuthorityToUser(String username, String role);

    /**
     * Update user
     *
     * @param username user username
     * @param userDto  user details
     */
    void updateUser(String username, UserDto userDto);

    /**
     * Delete user
     *
     * @param username user username
     */
    void deleteUser(String username);
}
