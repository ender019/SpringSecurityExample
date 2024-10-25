package com.server.autorization.JWTfuncs;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtCore jwtCore;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String username = null;
        UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken auth = null;
        try {
            Cookie[] cookieAuth = request.getCookies();
            try {
                jwt = cookieAuth[0].getValue();
            } catch (IndexOutOfBoundsException e) {
                String headerAuth = request.getHeader("Authorization");
                if(headerAuth != null && headerAuth.startsWith("Bearer ")) {
                    jwt = headerAuth.substring(7);
                }
            }

            if(jwt != null) {
                try {
                    username = jwtCore.getNameFromJwt(jwt);
                } catch (ExpiredJwtException e) {
                    System.out.println(e.getMessage());
                }
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    userDetails = userDetailsService.loadUserByUsername(username);
                    auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), null);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println(SecurityContextHolder.getContext().getAuthentication());
                }
            }
        } catch (Exception a) {
            System.out.println(a.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
