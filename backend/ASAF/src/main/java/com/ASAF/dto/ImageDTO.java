package com.ASAF.dto;

import com.ASAF.entity.ImageEntity;
import lombok.*;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageDTO {
    private Long image_id;
    private String image_url;
    private Long post_id;

    public ImageDTO(ImageEntity imageEntity) {
        this.image_id = imageEntity.getImage_id();
        this.image_url = imageEntity.getImage_url();
    }
}
