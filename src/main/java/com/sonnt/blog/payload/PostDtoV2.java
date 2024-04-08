package com.sonnt.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PostDtoV2 {
    private Long id;

    //add information more in swagger
    @Schema(
            description = "Blog Post Title"
    )
    //not empty or null
    // should have at least 2 char
    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;

    //add information more in swagger
    @Schema(
            description = "Blog Post Description"
    )
    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters")
    private String description;

    @Schema(
            description = "Blog Post Content"
    )
    @NotEmpty(message = "Post content should not empty")
    private String content;

    //add information more in swagger
    @Schema(
            description = "Blog Post Comments"
    )
    private Set<CommentDto> comments;


    private List<String> tags;
}
