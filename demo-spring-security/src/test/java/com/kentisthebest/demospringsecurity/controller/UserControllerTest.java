package com.kentisthebest.demospringsecurity.controller;

import com.kentisthebest.demospringsecurity.dto.UserDto;
import com.kentisthebest.demospringsecurity.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    void getUsers() throws Exception {
        // given
        int page = 0;
        int size = 100;

        List<UserDto> userDtos = Collections.singletonList(new UserDto());

        Page<UserDto> userDtoPage = new PageImpl<>(userDtos);

        given(userService.getUsers(page, size)).willReturn(userDtoPage);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .queryParam("page", String.valueOf(page))
                        .queryParam("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    void getUsers_whenCaughtException_thenThrowResponseStatusException() throws Exception {
        // given
        int page = 0;
        int size = 100;

        RuntimeException exception = new RuntimeException("something went wrong");

        willThrow(exception).given(userService).getUsers(page, size);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .queryParam("page", String.valueOf(page))
                        .queryParam("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest());
    }


    @Test
    void getUser() throws Exception {
        // given
        String username = "admin";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);

        given(userService.getUser(username)).willReturn(userDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/user/" + username)
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    void getUser_whenCaughtUsernameNotFoundException_thenThrowResponseStatusException() throws Exception {
        // given
        String username = "admin";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);

        UsernameNotFoundException exception =
                new UsernameNotFoundException("user not found in the database");

        willThrow(exception).given(userService).getUser(username);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/user/" + username)
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isNoContent());
    }

    @Test
    void addAuthorityToUser() throws Exception {
        // given
        String username = "admin";
        String authority = "ROLE_ADMIN";

        willDoNothing().given(userService).addAuthorityToUser(username, authority);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/user/authority/" + username + "/" + authority)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isCreated());
    }

    @Test
    void addAuthorityToUser_whenCaughtUsernameNotFoundException_thenThrowResponseStatusException() throws Exception {
        // given
        String username = "admin";
        String authority = "ROLE_ADMIN";

        UsernameNotFoundException exception =
                new UsernameNotFoundException("user not found in the database");

        willThrow(exception).given(userService).addAuthorityToUser(username, authority);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/user/authority/" + username + "/" + authority)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isNoContent());
    }

    @Test
    void addAuthorityToUser_whenCaughtException_thenThrowResponseStatusException() throws Exception {
        // given
        String username = "admin";
        String authority = "ROLE_ADMIN";

        RuntimeException exception = new RuntimeException("something went wrong");

        willThrow(exception).given(userService).addAuthorityToUser(username, authority);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/user/authority/" + username + "/" + authority)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveUser() throws Exception {
        // given
        UserDto userDto = new UserDto();

        willDoNothing().given(userService).saveUser(userDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isCreated());
    }

    @Test
    void saveUser_whenCaughtException_thenThrowResponseStatusException() throws Exception {
        // given
        UserDto userDto = new UserDto();

        RuntimeException exception = new RuntimeException("something went wrong");

        willThrow(exception).willDoNothing().given(userService).saveUser(userDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser() throws Exception {
        // given
        String username = "admin";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);

        willDoNothing().given(userService).updateUser(username, userDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/user/" + username)
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_whenCaughtUsernameNotFoundException_thenThrowResponseStatusException() throws Exception {
        // given
        String username = "admin";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);

        UsernameNotFoundException exception =
                new UsernameNotFoundException("user not found in the database");

        willThrow(exception).willDoNothing().given(userService).updateUser(username, userDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/user/" + username)
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isNoContent());
    }

    @Test
    void updateUser_whenCaughtException_thenThrowResponseStatusException() throws Exception {
        // given
        String username = "admin";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);

        RuntimeException exception = new RuntimeException("something went wrong");

        willThrow(exception).willDoNothing().given(userService).updateUser(username, userDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/user/" + username)
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser() throws Exception {
        // given
        String username = "admin";

        willDoNothing().given(userService).deleteUser(username);

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/" + username)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_whenCaughtUsernameNotFoundException_thenThrowResponseStatusException() throws Exception {
        // given
        String username = "admin";

        UsernameNotFoundException exception =
                new UsernameNotFoundException("user not found in the database");

        willThrow(exception).willDoNothing().given(userService).deleteUser(username);

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/" + username)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_whenCaughtException_thenThrowResponseStatusException() throws Exception {
        // given
        String username = "admin";

        RuntimeException exception = new RuntimeException("something went wrong");

        willThrow(exception).willDoNothing().given(userService).deleteUser(username);

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/" + username)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest());
    }
}
