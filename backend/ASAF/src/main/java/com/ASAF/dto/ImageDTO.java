package com.ASAF.dto;

import com.ASAF.entity.ImageEntity;
import lombok.*;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageDTO {
    private Long id;
    private String imageUri;
    private Long postId;

    public ImageDTO(ImageEntity imageEntity) {
        this.id = imageEntity.getId();
        this.imageUri = imageEntity.getImageUri();
        this.postId = imageEntity.getPost().getPost_id();
    }
}
