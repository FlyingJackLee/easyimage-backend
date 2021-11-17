package com.lizumin.easyimage.model.entity;

import com.auth0.jwt.JWT;
import com.sun.istack.NotNull;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import javax.persistence.*;
import java.util.*;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/11 12:57 am
 * 4
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    @NotNull
    private String username;

    @Column(nullable = false)
    private String password;


    @Column(name = "locale",nullable = false)
    private Locale locale;

    @Column(name = "email",nullable = false)
    private String email;


    @ManyToMany
    @JoinTable(name = "users_roles")
    private Set<Role> roles;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

}