package com.lizumin.easyimage.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 12:29 am
 * 4
 */
@Entity
@Table
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Account {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "jpa-uuid")
    private long userid;

    @Column
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @OneToMany(mappedBy = "account")
    private List<ImageLibrary> libraries;


    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userid +
                ", name='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", libraries=" + libraries +
                '}';
    }
}

