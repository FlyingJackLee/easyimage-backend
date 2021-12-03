package com.lizumin.easyimage.model.entity;

import com.lizumin.easyimage.constant.enums.ImageState;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 12:43 am
 * 4
 */
@Entity
@Table
public class LabelImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String filePath;
    @Column
    private ImageState state;
    @Column
    private String location;
    @Column
    private Date uploadDate;

    @Column
    private String name;


    @ManyToOne
    @JoinColumn(name = "library_id")
    private ImageLibrary imageLibrary;

    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private List<Tag> tags;

    public Long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public ImageState getState() {
        return state;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public ImageLibrary getImageLibrary() {
        return imageLibrary;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setState(ImageState state) {
        this.state = state;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setImageLibrary(ImageLibrary imageLibrary) {
        this.imageLibrary = imageLibrary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Date getUploadDate() {
        return uploadDate;
    }


}
