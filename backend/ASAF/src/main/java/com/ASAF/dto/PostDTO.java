package com.ASAF.dto;

import com.ASAF.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDTO {
    private Long post_id;
    private long register_time;
    private String title;
    private String content;
    private String profile_image;
    private String name;
    private int id;
    private List<ImageDTO> images;

    public PostDTO(PostEntity postEntity) {
        this.post_id = postEntity.getPost_id();
        this.register_time = postEntity.getRegister_time();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.profile_image = postEntity.getProfile_image();
        this.name = postEntity.getName();
        this.id = postEntity.getId();
    }
}
