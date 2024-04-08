package com.sonnt.blog.repository;

import com.sonnt.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryId(Long categoryId);
}
