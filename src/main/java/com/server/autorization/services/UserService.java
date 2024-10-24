package com.server.autorization.services;

import com.server.autorization.JWTfuncs.UserDetailsImpl;
import com.server.autorization.models.User;
import com.server.autorization.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Value("${service.connect.username}")
    private String service_username;

    @Value("${service.connect.password}")
    private String service_password;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (service_username.equals(username)) return UserDetailsImpl.build(new User(service_username, service_password));
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' nor found", username)
        ));
        return UserDetailsImpl.build(user);
    }
}
