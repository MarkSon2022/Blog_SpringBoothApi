package com.sonnt.blog.service;

import com.sonnt.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(Long postId);

    CommentDto getCommentByIdAndPostId(Long commentId, Long postId);

    CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest);

    void deleteComment(Long postId, Long commentId);
}
