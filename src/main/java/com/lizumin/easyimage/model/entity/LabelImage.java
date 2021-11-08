package com.lizumin.easyimage.model.entity;

import com.lizumin.easyimage.constant.enums.ImageState;

import javax.persistence.*;
import java.util.Date;
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
    private Date takenDate;


    @ManyToOne
    @JoinColumn(name = "library_id")
    private ImageLibrary imageLibrary;


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

    public Date getTakenDate() {
        return takenDate;
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

    public void setTakenDate(Date takenDate) {
        this.takenDate = takenDate;
    }

    public void setImageLibrary(ImageLibrary imageLibrary) {
        this.imageLibrary = imageLibrary;
    }
}
