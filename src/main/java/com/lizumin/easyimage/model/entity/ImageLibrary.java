package com.lizumin.easyimage.model.entity;


import javax.persistence.*;
import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 12:42 am
 * 4
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","library_name"}))
public class ImageLibrary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "library_name")
    private String name;

//    @OneToMany(mappedBy = "imageLibrary")
//    private List<LabelImage> labelImages;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
}
