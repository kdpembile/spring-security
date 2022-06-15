package com.example.demospringsecurity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityIdDto {

    @Mapping("username")
    @JsonProperty("username")
    private UserDto userDto;

    @Mapping("authority")
    @JsonProperty("authority")
    private String authority;
}
