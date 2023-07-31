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


    private int class_code;
    private int region_code;
    private int generation_code;
    private int Id;


    public static ClassInfoDTO toClassInfoDTO(ClassInfoEntity classInfoEntity) {
        ClassInfoDTO classInfoDTO = new ClassInfoDTO();
        classInfoDTO.setClass_num(classInfoEntity.getClass_num());
        classInfoDTO.setClass_code(classInfoEntity.getClass_code().getClass_code());
        classInfoDTO.setRegion_code(classInfoEntity.getRegion_code().getRegion_code());
        classInfoDTO.setGeneration_code(classInfoEntity.getGeneration_code().getGeneration_code());
        classInfoDTO.setId(classInfoEntity.getId().getId());
        return classInfoDTO;
    }

}