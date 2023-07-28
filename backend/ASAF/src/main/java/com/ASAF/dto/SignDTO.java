package com.ASAF.dto;

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
    private int doc_id;
    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private String user_id;
    private String image_url;
    private String name;
    private String month;

    public SignDTO(SignEntity signEntity) {
        this.sign_id = signEntity.getSign_id();
        this.image_url = signEntity.getImage_url();
        this.name = signEntity.getName();
        this.month = signEntity.getMonth();
    }

    public static SignDTO toSignDTO(SignEntity signEntity) {
        SignDTO signDTO = new SignDTO();
        signDTO.setSign_id(signEntity.getSign_id());
        signDTO.setImage_url(signEntity.getImage_url());
        signDTO.setName(signEntity.getName());
        signDTO.setMonth(signEntity.getMonth());
        return signDTO;
    }
}