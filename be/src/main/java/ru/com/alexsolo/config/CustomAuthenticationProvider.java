package ru.com.alexsolo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.com.alexsolo.domain.User;
import ru.com.alexsolo.repository.UserRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        String regexEmail = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        String regexPassword = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}";

        Pattern patternName = Pattern.compile(regexEmail);
        Matcher matcher = patternName.matcher(name);

        Pattern patternPassword = Pattern.compile(regexPassword);
        Matcher matcherPassword = patternPassword.matcher(password);


        try {
//            if(matcherPassword.matches() && matcher.matches()){
//
//            }
            User user = userRepository.getUser(name);
            if (user.getEmail().equals(name) && user.getPassword().equals(password)) {

                List<GrantedAuthority> roles = Stream.of(
                        new SimpleGrantedAuthority(String.valueOf(user.getRole()))
                ).collect(Collectors.toList());

                return new UsernamePasswordAuthenticationToken(name, password, roles);
            }else {
                System.out.println("не прошла авторизация");
            }
        }catch (NoResultException e){
            return null;
        }

        return null;
    }



    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}

