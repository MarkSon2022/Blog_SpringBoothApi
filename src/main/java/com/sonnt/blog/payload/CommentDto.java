package com.sonnt.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;
    @NotEmpty(message = "Comment, Name should not be empty")
    @Size(min = 2, message = "Comment, Name should have at least 2 characters")
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty(message = "Comment, Body should not empty")
    @Size(min = 10, message = "Comment, Body should have at least 2 characters")
    private String body;

}
