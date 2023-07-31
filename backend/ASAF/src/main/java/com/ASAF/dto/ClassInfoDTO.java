package com.ASAF.dto;

import com.ASAF.entity.ClassEntity;
import com.ASAF.entity.ClassInfoEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassInfoDTO {
    private int class_num;


    private ClassDTO class_code;
    private RegionDTO region_code;
    private GenerationDTO generation_code;
    private MemberDTO memberDTO;


    public static ClassInfoDTO toClassInfoDTO(ClassInfoEntity classInfoEntity) {
        ClassInfoDTO classInfoDTO = new ClassInfoDTO();
        classInfoDTO.setMemberDTO(MemberDTO.toMemberDTO(classInfoEntity.getId()));
        return classInfoDTO;
    }

}