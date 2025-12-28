package org.example.springbootdemo.service;

public interface IJwtService {
    String generateToken(String userName);

    boolean validateToken(String token);

    String getUsernameFromToken(String token);
}
