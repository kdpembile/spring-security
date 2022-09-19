package com.kentisthebest.demospringsecurity.dao;

import com.kentisthebest.demospringsecurity.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<UserEntity, String> {

    UserEntity findByUsername(String username);
}
