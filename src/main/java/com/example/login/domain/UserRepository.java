package com.example.login.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.login.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

}

