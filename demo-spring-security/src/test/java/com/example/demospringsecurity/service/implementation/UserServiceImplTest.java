package com.example.demospringsecurity.service.implementation;

import com.example.demospringsecurity.dao.AuthorityDao;
import com.example.demospringsecurity.dao.UserDao;
import com.example.demospringsecurity.dto.UserDto;
import com.example.demospringsecurity.entity.AuthorityEntity;
import com.example.demospringsecurity.entity.AuthorityId;
import com.example.demospringsecurity.entity.UserEntity;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Mock
    private AuthorityDao authorityDao;

    @Mock
    private Mapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void saveUser() {
        // given
        String username = "KD";
        String authority = "ROLE_ADMIN";

        UserDto userDto = mock(UserDto.class);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("secret");
        userEntity.setEnabled(true);

        Set<AuthorityEntity> authorityEntities = new HashSet<>();
        authorityEntities.add(new AuthorityEntity(
                new AuthorityId(userEntity, authority)));

        userEntity.setAuthority(authorityEntities);


        given(mapper.map(userDto, UserEntity.class))
                .willReturn(userEntity);

        given(userDao.saveAndFlush(userEntity))
                .willReturn(userEntity);

        // when
        userService.saveUser(userDto);

        // then
        then(userDao).should().saveAndFlush(userEntity);
    }

    @Test
    void getUser() {
        // given
        String username = "KD";
        String authority = "ROLE_ADMIN";

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("secret");
        userEntity.setEnabled(true);

        Set<AuthorityEntity> authorityEntities = new HashSet<>();
        authorityEntities.add(new AuthorityEntity(
                new AuthorityId(userEntity, authority)));

        userEntity.setAuthority(authorityEntities);

        given(userDao.existsById(anyString()))
                .willReturn(true);

        given(userDao.findByUsername(anyString()))
                .willReturn(userEntity);

        // when
        userService.getUser(anyString());

        // then
        then(userDao).should().existsById(anyString());
        then(userDao).should().findByUsername(anyString());
    }

    @Test
    void getUsers() {
        // given
        int page = 0;
        int size = 10000;

        List<UserEntity> userEntities = new ArrayList<>();

        Page<UserEntity> userEntityPage = new PageImpl<>(userEntities);

        Pageable pageable = PageRequest.of(page, size);

        given(userDao.findAll(pageable)).willReturn(userEntityPage);

        // when
        userService.getUsers(page, size);

        // then
        then(userDao).should().findAll(pageable);
    }

    @Test
    void addAuthorityToUser() {
        // given
        String username = "KD";
        String authority = "ROLE_ADMIN";

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("secret");
        userEntity.setEnabled(true);

        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthorityId(new AuthorityId(userEntity, authority));

        given(userDao.existsById(username)).willReturn(true);

        given(userDao.findByUsername(username)).willReturn(userEntity);

        given(authorityDao.saveAndFlush(authorityEntity))
                .willReturn(authorityEntity);

        // when
        userService.addAuthorityToUser(username, authority);

        // then
        then(userDao).should().findByUsername(anyString());
        then(authorityDao).should().saveAndFlush(authorityEntity);
    }

    @Test
    void updateUser() {
        // given
        String username = "KD";
        String authority = "ROLE_ADMIN";

        UserDto userDto = new UserDto();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("secret");
        userEntity.setEnabled(true);

        Set<AuthorityEntity> authorityEntities = new HashSet<>();
        authorityEntities.add(new AuthorityEntity(
                new AuthorityId(userEntity, authority)));

        userEntity.setAuthority(authorityEntities);

        given(userDao.existsById(username)).willReturn(true);

        willDoNothing().given(userDao).deleteById(username);

        given(mapper.map(userDto, UserEntity.class)).willReturn(userEntity);

        given(userDao.saveAndFlush(userEntity)).willReturn(userEntity);

        // when
        userService.updateUser(username, userDto);

        // then
        then(userDao).should().existsById(username);
        then(userDao).should().deleteById(username);
        then(mapper).should().map(userDto, UserEntity.class);
        then(userDao).should().saveAndFlush(userEntity);
    }

    @Test
    void deleteUser() {
        // given
        String username = "KD";

        given(userDao.existsById(username)).willReturn(true);

        willDoNothing().given(userDao).deleteById(username);

        // when
        userService.deleteUser(username);

        // then
        then(userDao).should().existsById(username);
        then(userDao).should().deleteById(username);
    }
}