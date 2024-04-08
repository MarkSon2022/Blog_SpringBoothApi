package com.sonnt.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//will execute before security filters
//basically provides the application for per request.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    //execute one per request
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        //get jwt token from request
        String token = getTokenFromRequest(request);
        //validate token
        //check null or empty && valid token
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            //get username from jwt token
            String username = jwtTokenProvider.getUsernameToken(token);

            // get the user object from the database
            // load the user associated with token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //instance of authentication token.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            // add this request object to this authentication token.
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //call security context holder and
            // then get context and then set authentication
            // then pass this authentication token
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        //call filter chain object
        filterChain.doFilter(request, response);
    }

    //get jwt from header with: "Bearer token"
    private String getTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }
}
