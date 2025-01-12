package com.spring.springbootapplication.repository;

import com.spring.springbootapplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository <UserEntity, Long> {
    UserEntity findByEmail(String email);
    
}
