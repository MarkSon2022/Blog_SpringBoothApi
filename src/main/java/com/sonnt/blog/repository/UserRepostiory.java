package com.sonnt.blog.repository;

import com.sonnt.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepostiory extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    //check if username existed
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
