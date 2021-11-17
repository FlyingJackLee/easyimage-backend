package com.lizumin.easyimage.model.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/10 9:56 pm
 * 4
 */
@Entity(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @ElementCollection(targetClass = String.class)
    @JoinTable(name = "roles_permission")
    private Set<String> permissions;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}