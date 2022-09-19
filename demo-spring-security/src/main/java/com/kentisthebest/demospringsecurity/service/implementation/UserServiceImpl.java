package com.kentisthebest.demospringsecurity.service.implementation;

import com.kentisthebest.demospringsecurity.dao.AuthorityDao;
import com.kentisthebest.demospringsecurity.dao.UserDao;
import com.kentisthebest.demospringsecurity.dto.UserDto;
import com.kentisthebest.demospringsecurity.entity.AuthorityEntity;
import com.kentisthebest.demospringsecurity.entity.AuthorityId;
import com.kentisthebest.demospringsecurity.entity.UserEntity;
import com.kentisthebest.demospringsecurity.service.UserService;
import com.github.dozermapper.core.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.List;

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

    public static final String USER_NOT_FOUND = "User not found in the database";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userDao.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userEntity.getAuthority().forEach(authority ->
                authorities.add(new SimpleGrantedAuthority(authority.getAuthorityId().getAuthority()))
        );
        return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
    }

    @Override
    public void saveUser(UserDto user) {
        log.info("Saving user {} ...", user.getUsername());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity userEntity = mapper.map(user, UserEntity.class);

        userEntity.getAuthority().forEach(authority ->
                authority.getAuthorityId().setUsername(userEntity));

        userDao.saveAndFlush(userEntity);

        log.info("{} was successfully saved and flushed", userEntity.getUsername());
    }

    @Override
    public UserDto getUser(String username) {
        if (!userDao.existsById(username)) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        UserEntity userEntity = userDao.findByUsername(username);

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public Page<UserDto> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<UserEntity> userEntities = userDao.findAll(pageable);

        List<UserDto> userDtos = userEntities.getContent().stream()
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .toList();

        return new PageImpl<>(userDtos, pageable, userDtos.size());
    }

    @Override
    public void addAuthorityToUser(String username, String authority) throws UsernameNotFoundException {
        log.info("Adding authority of {} to user {}", authority, username);

        if (!userDao.existsById(username)) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        UserEntity userEntity = userDao.findByUsername(username);

        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthorityId(new AuthorityId(userEntity, authority));

        authorityDao.saveAndFlush(authorityEntity);

        log.info("Authority of {} was successfully saved to user {}"
                , authority, username);
    }

    @Override
    public void updateUser(String username, UserDto user) {
        log.info("Updating user {}", username);

        if (!userDao.existsById(username)) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        userDao.deleteById(username);

        UserEntity userEntity = mapper.map(user, UserEntity.class);

        userEntity.getAuthority().forEach(authority ->
                authority.getAuthorityId().setUsername(userEntity));

        userDao.saveAndFlush(userEntity);

        log.info("User {} was successfully updated to user {}"
                , username, user.getUsername());
    }

    @Override
    public void deleteUser(String username) {
        log.info("Deleting user {}", username);

        if (!userDao.existsById(username)) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        userDao.deleteById(username);

        log.info("User {} was successfully deleted", username);
    }
}