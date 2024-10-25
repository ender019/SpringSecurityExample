package com.server.autorization.models;

import com.server.autorization.JWTfuncs.UserDetailsImpl;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String role;

    public User(String username, String password){
        this.id = (long)-1;
        this.username = username;
        this.email = "Bebecon@service.ru";
        this.password = password;
        this.role = "SERVICE";
    }

}
