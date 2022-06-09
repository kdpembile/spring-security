package com.example.demospringsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AuthorityId implements Serializable {

    @Serial
    private static final long serialVersionUID = 7078255232875863561L;

    @ManyToOne
    private UserEntity username;

    private String authority;
}
