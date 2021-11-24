package com.lizumin.easyimage.model.entity;


import javax.persistence.*;
import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 12:42 am
 * 4
 */
@Entity
@Table
public class ImageLibrary {

    @Id
    @Column(name = "id")
    private String id;

    @Column
    private String library_name;

    @OneToMany(mappedBy = "imageLibrary")
    private List<LabelImage> labelImages;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getLibrary_name() {
        return library_name;
    }

    public String getId() {
        return id;
    }

    public void setLibrary_name(String library_name) {
        this.library_name = library_name;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
