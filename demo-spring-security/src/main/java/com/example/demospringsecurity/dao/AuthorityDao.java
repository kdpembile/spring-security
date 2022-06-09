package com.example.demospringsecurity.dao;

import com.example.demospringsecurity.entity.AuthorityEntity;
import com.example.demospringsecurity.entity.AuthorityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityDao extends JpaRepository<AuthorityEntity, AuthorityId> {

}
