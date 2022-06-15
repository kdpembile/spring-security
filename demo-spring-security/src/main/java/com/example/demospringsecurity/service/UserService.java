package com.example.demospringsecurity.service;

import com.example.demospringsecurity.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    /**
     * Save new user
     *
     * @param user user details
     */
    void saveUser(UserDto user);

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
     * @param username user username
     * @param authority user authority
     */
    void addAuthorityToUser(String username, String authority) throws UsernameNotFoundException;

    /**
     * Update user
     *
     * @param username user username
     * @param user user details
     */
    void updateUser(String username, UserDto user);

    /**
     * Delete user
     *
     * @param username user username
     */
    void deleteUser(String username);
}
