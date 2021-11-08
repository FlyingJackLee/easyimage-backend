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
    @JoinColumn(name = "account_id")
    private Account account;

}
