package com.sonnt.blog.service;

import com.sonnt.blog.payload.LoginDto;
import com.sonnt.blog.payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
