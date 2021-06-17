package ru.com.alexsolo.config;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import ru.com.alexsolo.domain.User;
import ru.com.alexsolo.repository.UserRepository;

import javax.persistence.NoResultException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtTokenFilter extends GenericFilterBean {

    private JwtProwider jwtProwider = new JwtProwider();

    private UserRepository userRepository;

    public JwtTokenFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            List<Cookie> cookies = Arrays.asList(request.getCookies());
            Cookie cookieObj = cookies.stream().filter(cookie -> cookie.getName().equals("JWT")).findFirst().orElse(null);

            if (cookieObj.getValue() == null){
                SecurityContextHolder.getContext().setAuthentication(null);
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }

            String name = null;
            try {
                name = jwtProwider.validatorToken(cookieObj.getValue());
            }catch (SignatureVerificationException e){
                SecurityContextHolder.getContext().setAuthentication(null);
            }

            User user = null;
            try {
                 user = userRepository.getUser(name);
            }catch (NoResultException e){
                SecurityContextHolder.getContext().setAuthentication(null);
            }

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(token);

        }catch (NullPointerException e){

            SecurityContextHolder.getContext().setAuthentication(null);
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
