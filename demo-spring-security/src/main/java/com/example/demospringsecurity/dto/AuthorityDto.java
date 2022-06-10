package com.example.demospringsecurity.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto {

    @Mapping("authorityId")
    private AuthorityIdDto authorityId;
}
