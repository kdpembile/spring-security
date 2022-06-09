package com.example.demospringsecurity.dao;

import com.example.demospringsecurity.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<UserEntity, String> {

    UserEntity findByUsername(String username);
}
