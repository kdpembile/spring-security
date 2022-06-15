package com.example.demospringsecurity.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto {

    @Mapping("authorityId")
    @JsonUnwrapped
    private AuthorityIdDto authorityId;
}
