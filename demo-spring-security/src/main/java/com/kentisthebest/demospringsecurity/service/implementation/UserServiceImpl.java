package com.kentisthebest.demospringsecurity.service.implementation;

import com.github.dozermapper.core.Mapper;
import com.kentisthebest.demospringsecurity.dao.AuthorityDao;
import com.kentisthebest.demospringsecurity.dao.UserDao;
import com.kentisthebest.demospringsecurity.dto.UserDto;
import com.kentisthebest.demospringsecurity.entity.AuthorityEntity;
import com.kentisthebest.demospringsecurity.entity.AuthorityId;
import com.kentisthebest.demospringsecurity.entity.UserEntity;
import com.kentisthebest.demospringsecurity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

import static com.kentisthebest.demospringsecurity.error.ErrorHandler.USER_NOT_FOUND;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthorityDao authorityDao;

    @Autowired
    private Mapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getAuthority().forEach(authority ->
                authorities.add(new SimpleGrantedAuthority(authority.getAuthorityId().getAuthority()))
        );
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
    }

    @Override
    public void saveUser(UserDto userDto) {
        log.info("Saving user {} ...", userDto.getUsername());

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        UserEntity user = mapper.map(userDto, UserEntity.class);

        user.getAuthority().forEach(authority ->
                authority.getAuthorityId().setUsername(user));

        userDao.saveAndFlush(user);

        log.info("{} was successfully saved and flushed", user.getUsername());
    }

    @Override
    public UserDto getUser(String username) {
        return userDao.findByUsername(username)
                .map(user -> mapper.map(user, UserDto.class))
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public Page<UserDto> getUsers(int page, int size) {
        return userDao.findAll(PageRequest.of(page, size))
                .map(user -> mapper.map(user, UserDto.class));
    }

    @Override
    public void addAuthorityToUser(String username, String role) {
        log.info("Adding authority of {} to user {}", role, username);

        UserEntity user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthorityId(new AuthorityId(user, role));

        authorityDao.saveAndFlush(authorityEntity);

        log.info("Authority of {} was successfully added to user {}"
                , role, username);
    }

    @Override
    public void updateUser(String username, UserDto userDto) {
        log.info("Updating user {}", username);

        userDao.findByUsername(username)
                .ifPresentOrElse(user -> userDao.deleteById(user.getUsername()),
                        () -> {
                            throw new UsernameNotFoundException(USER_NOT_FOUND);
                        });

        UserEntity user = mapper.map(userDto, UserEntity.class);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.getAuthority().forEach(authority ->
                authority.getAuthorityId().setUsername(user));

        userDao.saveAndFlush(user);

        if (username.equals(user.getUsername())) {
            log.info("User {} was successfully updated", username);

            return;
        }

        log.info("User {} was successfully updated to user {}"
                , username, user.getUsername());
    }

    @Override
    public void deleteUser(String username) {
        log.info("Deleting user {}", username);

        userDao.findByUsername(username)
                .ifPresentOrElse(user -> userDao.deleteById(user.getUsername()),
                        () -> {
                            throw new UsernameNotFoundException(USER_NOT_FOUND);
                        });

        userDao.flush();

        log.info("User {} was successfully deleted", username);
    }
}