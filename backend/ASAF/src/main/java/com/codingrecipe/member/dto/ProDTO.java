package com.codingrecipe.member.dto;

import com.codingrecipe.member.entity.ProEntity;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProDTO {
    private Long id;
    private String name;
    private Long pro_number;
    private String email;
    private String phone_number;
    private String profile_image;

    public static ProDTO toProDTO(ProEntity proEntity) {
        ProDTO proDTO = new ProDTO();
        proDTO.setId(proEntity.getId());
        proDTO.setName(proEntity.getName());
        proDTO.setPro_number(proEntity.getPro_number());
        proDTO.setEmail(proEntity.getEmail());
        proDTO.setPhone_number(proEntity.getPhone_number());
        proDTO.setProfile_image(proEntity.getProfile_image());
        return proDTO;
    }
}
