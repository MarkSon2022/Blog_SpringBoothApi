package com.sonnt.blog.service.impl;

import com.sonnt.blog.entity.Role;
import com.sonnt.blog.entity.User;
import com.sonnt.blog.exception.BlogAPIException;
import com.sonnt.blog.payload.LoginDto;
import com.sonnt.blog.payload.RegisterDto;
import com.sonnt.blog.repository.RoleRepostiory;
import com.sonnt.blog.repository.UserRepostiory;
import com.sonnt.blog.security.JwtTokenProvider;
import com.sonnt.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImp implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepostiory userRepostiory;
    private RoleRepostiory roleRepostiory;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImp(AuthenticationManager authenticationManager,
                          UserRepostiory userRepostiory,
                          RoleRepostiory roleRepostiory,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepostiory = userRepostiory;
        this.roleRepostiory = roleRepostiory;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //After successful authentication, we'll create a JWT token and we'll return it.
        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {

        //add check for username exists in database
        if (userRepostiory.existsByUsername(registerDto.getUsername())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already existed!");
        }
        //add check for email exists in database
        if (userRepostiory.existsByEmail(registerDto.getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already existed!");
        }
        // create user
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        // create role for user
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepostiory.findByName("ROLE_USER").get();
        roles.add(userRole);
        //add roll to user
        user.setRoles(roles);
        //saving to database
        userRepostiory.save(user);

        return "User register is successful!!";
    }
}
