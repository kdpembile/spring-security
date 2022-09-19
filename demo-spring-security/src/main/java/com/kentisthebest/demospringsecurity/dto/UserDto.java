package com.kentisthebest.demospringsecurity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "User details")
public class UserDto {

    @Mapping("username")
    @ApiModelProperty(notes = "User's username")
    private String username;

    @Mapping("password")
    @ApiModelProperty(notes = "User's password")
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Mapping("enabled")
    @ApiModelProperty(notes = "This will tell if the user's account is enabled or not")
    private boolean enabled;

    @Mapping("authority")
    @ApiModelProperty(notes = "User's authorities")
    private Set<AuthorityDto> authority = new HashSet<>();
}
