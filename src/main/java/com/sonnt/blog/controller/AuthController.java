package com.sonnt.blog.controller;

import com.sonnt.blog.payload.JWTAuthResponse;
import com.sonnt.blog.payload.LoginDto;
import com.sonnt.blog.payload.RegisterDto;
import com.sonnt.blog.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
//add more information for Controller in swagger
@Tag(
        name = "CRUD REST APIs for AuthController"
)
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //Build Login rest Api
    //can use both url login or signin
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        //return token
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping(value = {"signup", "/register"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
