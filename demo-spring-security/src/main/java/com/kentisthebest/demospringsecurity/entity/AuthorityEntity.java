package com.kentisthebest.demospringsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities", indexes = {
        @Index(name = "ix_auth_username ", columnList = "username, authority", unique = true)
})
@AssociationOverride(name = "authorityId.username", joinColumns = @JoinColumn(name = "username"))
@AttributeOverride(name = "authorityId.authority", column = @Column(name = "authority", length = 50, nullable = false))
public class AuthorityEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7746123128493234320L;

    @EmbeddedId
    private AuthorityId authorityId;
}
