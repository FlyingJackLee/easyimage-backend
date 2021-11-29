package com.lizumin.easyimage.model.entity;

import javax.persistence.*;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/25 9:10 pm
 * 4
 */
@Entity
@Table
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String type;

    public Tag(String tag){
        this.type = tag;
    }

    public Tag() {

    }


    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }
}
