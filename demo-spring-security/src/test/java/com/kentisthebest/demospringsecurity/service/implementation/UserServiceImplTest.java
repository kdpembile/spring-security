package com.kentisthebest.demospringsecurity.service.implementation;

import com.github.dozermapper.core.Mapper;
import com.kentisthebest.demospringsecurity.dao.AuthorityDao;
import com.kentisthebest.demospringsecurity.dao.UserDao;
import com.kentisthebest.demospringsecurity.dto.AuthorityDto;
import com.kentisthebest.demospringsecurity.dto.AuthorityIdDto;
import com.kentisthebest.demospringsecurity.dto.UserDto;
import com.kentisthebest.demospringsecurity.entity.AuthorityEntity;
import com.kentisthebest.demospringsecurity.entity.AuthorityId;
import com.kentisthebest.demospringsecurity.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
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
    void loadUserByUsername() {
        // given
        UserEntity user = getUserEntity();
        String username = user.getUsername();

        Optional<UserEntity> opUser = Optional.of(user);

        given(userDao.findByUsername(username))
                .willReturn(opUser);

        // when
        userService.loadUserByUsername(username);

        // then
        then(userDao).should().findByUsername(username);
    }

    @Test
    void saveUser() {
        // given
        UserEntity user = getUserEntity();

        UserDto userDto = getUserDto();

        given(mapper.map(userDto, UserEntity.class))
                .willReturn(user);

        given(userDao.saveAndFlush(user))
                .willReturn(user);

        // when
        userService.saveUser(userDto);

        // then
        then(userDao).should().saveAndFlush(user);
    }

    @Test
    void getUser() {
        // given
        UserEntity user = getUserEntity();
        String username = user.getUsername();

        Optional<UserEntity> opUser = Optional.of(user);

        UserDto userDto = new UserDto();

        given(userDao.findByUsername(username))
                .willReturn(opUser);

        given(mapper.map(user, UserDto.class))
                .willReturn(userDto);
        // when
        userService.getUser(username);

        // then
        then(userDao).should().findByUsername(username);
    }

    @Test
    void getUsers() {
        // given
        int page = 0;
        int size = 10000;
        UserEntity user = getUserEntity();

        List<UserEntity> users = Collections.singletonList(user);

        Page<UserEntity> userPage = new PageImpl<>(users);

        Pageable pageable = PageRequest.of(page, size);

        given(userDao.findAll(pageable)).willReturn(userPage);

        // when
        userService.getUsers(page, size);

        // then
        then(userDao).should().findAll(pageable);
    }

    @Test
    void addAuthorityToUser() {
        // given
        UserEntity user = getUserEntity();
        String username = user.getUsername();

        AuthorityEntity authority = user
                .getAuthority()
                .stream()
                .findFirst()
                .orElse(any());

        String role = authority.getAuthorityId().getAuthority();

        Optional<UserEntity> opUser = Optional.of(user);

        given(userDao.findByUsername(username)).willReturn(opUser);

        given(authorityDao.saveAndFlush(authority))
                .willReturn(authority);

        // when
        userService.addAuthorityToUser(username, role);

        // then
        then(userDao).should().findByUsername(username);
        then(authorityDao).should().saveAndFlush(authority);
    }

    @Test
    void updateUser() {
        // given
        UserEntity user = getUserEntity();
        String username = user.getUsername();

        Optional<UserEntity> opUser = Optional.of(user);

        UserDto userDto = getUserDto();

        given(userDao.findByUsername(username))
                .willReturn(opUser);

        willDoNothing().given(userDao).deleteById(username);

        given(mapper.map(userDto, UserEntity.class))
                .willReturn(user);

        given(userDao.saveAndFlush(user))
                .willReturn(user);

        // when
        userService.updateUser(username, userDto);

        // then
        then(userDao).should().findByUsername(username);
        then(userDao).should().deleteById(username);
        then(mapper).should().map(userDto, UserEntity.class);
        then(userDao).should().saveAndFlush(user);
    }

    @Test
    void deleteUser() {
        // given
        UserEntity user = getUserEntity();
        String username = user.getUsername();

        Optional<UserEntity> opUser = Optional.of(user);

        given(userDao.findByUsername(username))
                .willReturn(opUser);

        willDoNothing().given(userDao).deleteById(username);

        // when
        userService.deleteUser(username);

        // then
        then(userDao).should().findByUsername(username);
        then(userDao).should().deleteById(username);
    }

    public UserEntity getUserEntity() {
        String username = "KD";
        String role = "ROLE_ADMIN";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("secret");
        user.setEnabled(true);

        AuthorityEntity authority = new AuthorityEntity();
        authority.setAuthorityId(new AuthorityId(user, role));

        user.setAuthority(Collections.singleton(authority));

        return user;
    }

    public UserDto getUserDto() {
        String username = "KD";
        String role = "ROLE_ADMIN";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword("secret");
        userDto.setEnabled(true);

        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.setAuthorityId(new AuthorityIdDto(role));

        userDto.setAuthority(Collections.singleton(authorityDto));

        return userDto;
    }
}