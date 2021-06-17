package ru.com.alexsolo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProwider {

    private String secret = "GenerateMorange";

    public String generationToken(String name){
       return JWT.create()
               .withSubject(name)
               .withExpiresAt(new Date(System.currentTimeMillis() + 864000000))
               .sign(Algorithm.HMAC512(secret.getBytes(StandardCharsets.UTF_8)));
    }

    public String validatorToken(String token){

         return  JWT.require(Algorithm.HMAC512(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .verify(token)
                .getSubject();

    }


}
