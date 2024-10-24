package com.server.autorization.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/secured")
public class MainController {
    @GetMapping("user")
    public String userAccess(Principal principal){
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
