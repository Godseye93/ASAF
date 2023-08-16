// PostEntity.java
package com.ASAF.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "post")
@ToString(exclude = "images")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @Column
    private long register_time;

    @Column
    private String title;

    @Column(length = 10000)
    private String content;

    private int id;

    @Column
    private String profile_image;

    @Column
    private String name;

    @OneToMany(mappedBy = "post")
    private List<ImageEntity> images;
}
