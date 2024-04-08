package com.sonnt.blog.service.impl;

import com.sonnt.blog.entity.Category;
import com.sonnt.blog.entity.Post;
import com.sonnt.blog.exception.ResourceNotFoundException;
import com.sonnt.blog.payload.PostDto;
import com.sonnt.blog.payload.PostResponse;
import com.sonnt.blog.repository.CategoryRepository;
import com.sonnt.blog.repository.PostRepository;
import com.sonnt.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImp implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;
    private CategoryRepository categoryRepository;

    @Autowired
    public PostServiceImp(PostRepository postRepository, ModelMapper mapper,
                          CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        //Set category to post
        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));
        //convert DTO to entity
        Post post = mapToEntity(postDto);
        //set to post
        post.setCategory(category);
        //convert entity to Dto
        Post newPost = postRepository.save(post);
        //
        PostDto postResponse = mapToDto(newPost);
        //
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        //sort to choose
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //create a Pageable instance
        // pagination
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);//auto asc
        //
        Page<Post> posts = postRepository.findAll(pageable);

        // get content from page objects
        List<Post> listOfPost = posts.getContent();

        //get content of post
        List<PostDto> content = listOfPost.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        // get post by id from the database
        Post post = postRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));
        // set value
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);
        //update
        Post updatePost = postRepository.save(post);
        return mapToDto(updatePost);
    }

    @Override
    public void deletePostById(Long id) {
        //get post by id from the database
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        //delete
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        List<Post> list = postRepository.findByCategoryId(categoryId);

        return list.stream()
                .map(post -> mapToDto(post))
                .collect(Collectors.toList());
    }

    //convert entity to Dto
    private PostDto mapToDto(Post post) {
        PostDto postDto = mapper.map(post, PostDto.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setContent(post.getContent());
//        postDto.setDescription(post.getDescription());
        return postDto;
    }

    //convert DTO to entity
    private Post mapToEntity(PostDto postDto) {
        Post post = mapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }

}
