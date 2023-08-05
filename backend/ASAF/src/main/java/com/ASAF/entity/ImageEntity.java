package com.ASAF.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "post_image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long image_id;

    @Column
    private String image_url;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;
}
