package com.ASAF.dto;

import com.ASAF.entity.ClassInfoEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassInfoDTO {
    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private MemberDTO memberDTO;

    public static ClassInfoDTO toClassInfoDTO(ClassInfoEntity classInfoEntity) {
        ClassInfoDTO classInfoDTO = new ClassInfoDTO();
        classInfoDTO.setMemberDTO(MemberDTO.toMemberDTO(classInfoEntity.getId()));
        return classInfoDTO;
    }
}