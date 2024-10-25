package com.server.autorization.controllers;

import com.server.autorization.JWTfuncs.JwtCore;
import com.server.autorization.models.User;
import com.server.autorization.repositories.UserRepository;
import com.server.autorization.requests.SigninRequest;
import com.server.autorization.requests.SignupRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://26.44.55.174:5173")
public class SecurityController {
    @Value("${service.connect.username}")
    private String service_username;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtCore jwtCore;

    @GetMapping("/test")
    ResponseEntity<?> test_session()
    {
        return ResponseEntity.ok("connected");
    }

    @PostMapping("/signin")
    ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest, HttpServletResponse response)
    {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        jakarta.servlet.http.Cookie cookie = new Cookie("token", jwt);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
        response.setContentType("text/plain");
        return ResponseEntity.ok("success signing in");
    }

    @PostMapping("/signup")
    ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsUserByUsername(signupRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different name");
        }
        if (userRepository.existsUserByEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different email");
        }
        String hash = passwordEncoder.encode(signupRequest.getPassword());

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(hash);
        userRepository.save(user);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/connect")
    ResponseEntity<?> service_connect(@RequestBody SigninRequest connectRequest, HttpServletResponse response)
    {
        if (service_username.equals(connectRequest.getUsername())) {
            Authentication authentication = null;
            try {
                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(connectRequest.getUsername(), connectRequest.getPassword()));
            } catch (BadCredentialsException e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtCore.generateToken(authentication);
            jakarta.servlet.http.Cookie cookie = new Cookie("token", jwt);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
            response.setContentType("text/plain");
            return ResponseEntity.ok("success connect, " + jwt);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
