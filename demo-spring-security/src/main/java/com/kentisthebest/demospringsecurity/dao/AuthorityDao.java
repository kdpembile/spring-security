package com.kentisthebest.demospringsecurity.dao;

import com.kentisthebest.demospringsecurity.entity.AuthorityEntity;
import com.kentisthebest.demospringsecurity.entity.AuthorityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityDao extends JpaRepository<AuthorityEntity, AuthorityId> {

}
