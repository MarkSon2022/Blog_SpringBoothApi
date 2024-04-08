package com.sonnt.blog.controller;

import com.sonnt.blog.payload.PostDto;
import com.sonnt.blog.payload.PostResponse;
import com.sonnt.blog.service.PostService;
import com.sonnt.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// use v1 for versioning url path
@RequestMapping()
//add more information for Controller in swagger
@Tag(
        name = "CRUD REST APIs for Post Controller"
)
public class PostController {

    private PostService postService;

    //if we have on constructor
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }


    //create blog post
    //add more information for api
    @Operation(
            summary = "Create Post REST API",
            description = "Create Post Rest API is used to save posts in database"
    )
    //API response annotation from swagger package
    // to provide the response
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    // add require for jwt authentication in swagger
    @SecurityRequirement(
            name = "Bear Authentication"//just use name from SecurityScheme in security config
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("api/v1/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }


    //get all post
    //add more information for api
    @Operation(
            summary = "Get All Posts REST API",
            description = "Get All Posts  REST API is used to fetch all posts from the database"
    )
    //API response annotation from swagger package
    // to provide the response
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("api/v1/posts")
    public PostResponse getAllPost(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }


    //get post by id
    //add more information for api
    @Operation(
            summary = "Get Post By Id REST API",
            description = "Get Post By Id REST API is used to get single post from the database"
    )
    //API response annotation from swagger package
    // to provide the response
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    //url
    @GetMapping("api/v1/posts/{id}")
    //parameter
    //@GetMapping(value = "api/posts/{id}", params = "version=1")
    //custom header versioning
    //@GetMapping(value = "api/posts/{id}",headers = "X-API-VERSION=1")
    //through content negotiation
    //@GetMapping(value = "api/posts/{id}", produces = "application/vnd.son.v1+json")
    public ResponseEntity<PostDto> getPostById(@PathVariable long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // VERSIONING URL CONTROL VERSION ------------------------------------------------
    //@GetMapping("api/v2/posts/{id}")
    //@GetMapping(value = "api/posts/{id}", params = "version=2")
    //@GetMapping(value = "api/posts/{id}", headers = "X-API-VERSION=2")
//    @GetMapping(value = "api/posts/{id}",produces = "application/vnd.son.v2+json")
//    public ResponseEntity<PostDtoV2> getPostByIdV2(@PathVariable long id) {
//        PostDto postDto= postService.getPostById(id);
//        PostDtoV2 postDtoV2= new PostDtoV2();
//
//        postDtoV2.setId(postDto.getId());
//        postDtoV2.setDescription(postDto.getDescription());
//        postDtoV2.setTitle(postDto.getTitle());
//        postDtoV2.setComments(postDto.getComments());
//        postDtoV2.setContent(postDto.getContent());
//        List<String> tags= new ArrayList<>();
//        tags.add("Java");
//        tags.add("Spring boot");
//        tags.add("AWS");
//        postDtoV2.setTags(tags);
//
//        return ResponseEntity.ok(postDtoV2);
//    }
    //--------------------------------------------------


    //update post by id
    //add more information for api
    @Operation(
            summary = "Update Post By Id REST API",
            description = "Update Post By Id REST API is used to update a particular post from the database"
    )
    //API response annotation from swagger package
    // to provide the response
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    // add require for jwt authentication in swagger
    @SecurityRequirement(
            name = "Bear Authentication"//just use name from SecurityScheme in security config
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("api/v1/posts//{id}")
    public ResponseEntity<PostDto> updatePostByid(@Valid @RequestBody PostDto postDto, @PathVariable long id) {
        return ResponseEntity.ok(postService.updatePost(postDto, id));
    }


    //delete post by id
    //add more information for api
    @Operation(
            summary = "Delete Post By Id REST API",
            description = "Delete Post By Id REST API is used to delete a particular post from the database"
    )
    //API response annotation from swagger package
    // to provide the response
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    // add require for jwt authentication in swagger
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("api/v1/posts/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable long id) {
        postService.deletePostById(id);
        return ResponseEntity.ok("Post entity deleted successfully");
    }


    //get post by category rest api
    //add more information for api
    @Operation(
            summary = "Get All Posts By Category REST API",
            description = "Get All Posts By Category  REST API is used to fetch all posts by category from the database"
    )
    //API response annotation from swagger package
    // to provide the response
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("api/v1/posts/category/{id}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable("id") Long categoryId) {
        List<PostDto> postDtos = postService.getPostByCategory(categoryId);
        return ResponseEntity.ok(postDtos);
    }
}
