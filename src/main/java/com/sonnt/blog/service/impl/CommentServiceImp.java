package com.sonnt.blog.service.impl;

import com.sonnt.blog.entity.Comment;
import com.sonnt.blog.entity.Post;
import com.sonnt.blog.exception.BlogAPIException;
import com.sonnt.blog.exception.ResourceNotFoundException;
import com.sonnt.blog.payload.CommentDto;
import com.sonnt.blog.repository.CommentRepository;
import com.sonnt.blog.repository.PostRepository;
import com.sonnt.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImp implements CommentService {


    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    @Autowired
    public CommentServiceImp(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        // map to entitys
        Comment comment = mapToEntity(commentDto);
        // retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        //check if comment has already exist

        //set post to comment entity
        comment.setPost(post);
        //save comment entity
        Comment newComment = commentRepository.save(comment);
        //return dto
        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        //retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        //convert to list Comments Dto
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentByIdAndPostId(Long commentId, Long postId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        //check if get the right comment with id
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "This comment with id: " + commentId + " does not belong to post with id: " + postId);
        }
        // convert to dto
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        // retrieve post entity
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        //check if get the right comment with id
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "This comment with id: " + commentId + " does not belong to post with id: " + postId);
        }
        //update
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());
        Comment updateComment = commentRepository.save(comment);
        //return convert
        return mapToDto(updateComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        // retrieve post entity
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        //check if get the right comment with id
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "This comment with id: " + commentId + " does not belong to post with id: " + postId);
        }
        //delete
        commentRepository.delete(comment);

    }


    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setBody(comment.getBody());
//        commentDto.setEmail(comment.getEmail());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment;
    }
}
