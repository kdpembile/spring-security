package com.example.demospringsecurity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Mapping("username")
    private String username;

    @Mapping("password")
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private String password;

    @Mapping("enabled")
    private boolean enabled;

    @Mapping("authority")
    private Set<AuthorityDto> authority = new HashSet<>();

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }
}
