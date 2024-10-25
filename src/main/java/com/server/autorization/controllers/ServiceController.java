package com.server.autorization.controllers;

import com.server.autorization.models.User;
import com.server.autorization.repositories.UserRepository;
import com.server.autorization.requests.SigninRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/service")
@CrossOrigin(origins = "http://26.44.55.174:5173")
public class ServiceController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/get_user_by_id/{id}")
    ResponseEntity<?> get_user_by_id(@PathVariable("id") Long id)
    {
        Optional<User> user = userRepository.findUserById(id);
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No this User");
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/get_user_by_username/{username}")
    ResponseEntity<?> get_user_by_name(@PathVariable("username") String username)
    {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No this User");
        return ResponseEntity.ok().body(user);
    }
}
