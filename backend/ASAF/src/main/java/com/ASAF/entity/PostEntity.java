package com.ASAF.entity;

import com.ASAF.dto.PostDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @Column
    private long register_time;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "id")
    private MemberEntity id;

    @Column
    private String profile_image;

    @Column
    private String name;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageEntity> images;

    public MemberEntity getMemberEntity() { return id; }

}
