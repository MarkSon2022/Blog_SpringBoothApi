package com.sonnt.blog.repository;

import com.sonnt.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    Comment findByIdAndPostId(Long commentId, Long postId);
}
