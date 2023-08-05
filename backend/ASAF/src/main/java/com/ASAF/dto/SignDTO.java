package com.ASAF.dto;

import com.ASAF.entity.SignEntity;
import com.ASAF.entity.SignEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignDTO {
    private Long sign_id;
    private String image_url;
    private String name;
    private String month;
    
    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private int id;

    public SignDTO(SignEntity signEntity) {
        this.sign_id = signEntity.getSign_id();
        this.image_url = signEntity.getImage_url();
        this.name = signEntity.getName();
        this.month = signEntity.getMonth();
        this.class_num = signEntity.getClassInfoEntity().getClass_num();
        this.class_code = signEntity.getClassEntity().getClass_code();
        this.region_code = signEntity.getRegionEntity().getRegion_code();
        this.generation_code = signEntity.getGenerationEntity().getGeneration_code();
        this.id = signEntity.getMemberEntity().getId();
    }
}