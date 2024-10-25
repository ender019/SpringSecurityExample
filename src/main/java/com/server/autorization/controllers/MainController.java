package com.server.autorization.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/secured")
@CrossOrigin(origins = "http://26.44.55.174:5173")
public class MainController {
    @GetMapping("user")
    public String userAccess(HttpServletRequest request, Principal principal){
        System.out.println(request.isUserInRole("ADMIN"));
        if(principal == null) return null;
        return principal.getName();
    }

    @GetMapping("admin")
    public String adminAccess(Principal principal){
        if(principal == null) return null;
        return "admin: " + principal.getName();
    }

    @GetMapping("service")
    public String serviceAccess(Principal principal){
        if(principal == null) return null;
        return "service: " + principal.getName();
    }
}
