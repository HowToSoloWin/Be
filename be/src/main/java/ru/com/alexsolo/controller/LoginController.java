package ru.com.alexsolo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import ru.com.alexsolo.Dto.ApplicationUser;
import ru.com.alexsolo.Dto.LoginSuccess;
import ru.com.alexsolo.config.JwtProwider;
import ru.com.alexsolo.domain.User;
import ru.com.alexsolo.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/login")
public class LoginController {

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtProwider jwtProwider;

    @Autowired
    public LoginController(UserRepository userRepository, AuthenticationManager authenticationManager, JwtProwider jwtProwider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProwider = jwtProwider;
    }

    @PostMapping()
    public ResponseEntity login(@RequestBody ApplicationUser applicationUser){

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(applicationUser.getName(),applicationUser.getPassword()));
            String token = jwtProwider.generationToken(applicationUser.getName());
            User user = userRepository.getUser(applicationUser.getName());
            Map<Object, Object> response = new HashMap<>();
            response.put("username",applicationUser.getName());
            response.put("firstName",user.getFirstName());
            response.put("role",user.getRole());
            ResponseCookie responseCookie = ResponseCookie.from("JWT",token)
                    .httpOnly(true)
                    .secure(true)
                    .maxAge(259200000*2)
                    .path("/")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString()).body(response);
        }catch (AuthenticationException e){
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @GetMapping("/success")
    public LoginSuccess getLog(){
        return new LoginSuccess(true);
    }



}
