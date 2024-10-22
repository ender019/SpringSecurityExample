package com.server.autorization.requests;

import lombok.Data;

@Data
public class SigninRequest {
    private String username;
    private String password;
}
