package com.example.demospringsecurity.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Mapping("username")
    private String username;

    @Mapping("password")
    private String password;

    @Mapping("enabled")
    private boolean enabled;

    @Mapping("authority")
    private Set<AuthorityDto> authority = new HashSet<>();
}
