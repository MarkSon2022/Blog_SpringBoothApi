package com.sonnt.blog.security;

import com.sonnt.blog.exception.BlogAPIException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    //got value form application
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app-jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    //generate JWT token
    public String generateToken(Authentication authentication) {

        String username = authentication.getName();
        Date currentDate = new Date();
        //get expire
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())
                .compact();

        return token;
    }

    //create common key
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //get username from Jwt Token
    public String getUsernameToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())//use key to verify
                .build()
                .parseSignedClaims(token)//pass the token
                .getPayload()
                .getSubject();//return username
    }

    //method to validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())//use key to verify
                    .build()
                    .parse(token);
            return true;
        }
        //Handle error ---------------------------------------------
        //valid
        catch (MalformedJwtException malformedJwtException) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        }
        //expire
        catch (ExpiredJwtException expiredJwtException) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expire JWT token");
        }
        //unsupported
        catch (UnsupportedJwtException unsupportedJwtException) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        }
        //illegal
        catch (IllegalArgumentException illegalArgumentException) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is null or empty");
        }
    }
}
