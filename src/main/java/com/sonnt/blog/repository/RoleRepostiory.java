package com.sonnt.blog.repository;

import com.sonnt.blog.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepostiory extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

}
