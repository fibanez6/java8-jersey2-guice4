package com.fibanez.jersey2.service;

public interface TokenService {

    String createJsonWebToken(String subject);

    String validateToken(String token);

}
