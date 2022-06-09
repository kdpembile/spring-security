package com.example.demospringsecurity.service.implementation;

import com.example.demospringsecurity.dao.AuthorityDao;
import com.example.demospringsecurity.dao.UserDao;
import com.example.demospringsecurity.dto.UserDto;
import com.example.demospringsecurity.entity.UserEntity;
import com.example.demospringsecurity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userDao.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userEntity.getAuthority().forEach(authority ->
                authorities.add(new SimpleGrantedAuthority(authority.getAuthorityId().getAuthority()))
        );
        return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
    }

    @Override
    public void saveUser(UserEntity user) {
        log.info("Saving user {} ...", user.getUsername());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.saveAndFlush(user);

        log.info("{} was successfully saved and flushed"
                , user.getUsername());
    }

    @Override
    public UserDto getUser(String username) {
        UserEntity user = userDao.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return mapper.map(user, UserDto.class);
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
    public void updateUser(String username, UserDto userDto) {
        log.info("Updating user {}", username);

        userDao.deleteById(username);

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        userDao.saveAndFlush(userEntity);

        log.info("User {} was successfully updated to user {}"
                , username, userDto.getUsername());
    }

    @Override
    public void deleteUser(String username) {
        log.info("Deleting user {}", username);

        userDao.deleteById(username);

        log.info("User {} was successfully deleted", username);
    }
}
