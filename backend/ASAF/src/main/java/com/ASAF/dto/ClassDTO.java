package com.ASAF.dto;

import com.ASAF.entity.ClassEntity;
import com.ASAF.entity.ClassEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassDTO {
    private int class_code;
    private String classname;

    public static ClassDTO toClassDTO(ClassEntity classEntity) {
        ClassDTO classDTO = new ClassDTO();
        classDTO.setClass_code(classEntity.getClass_code());
        classDTO.setClassname(classEntity.getClassname());
        return classDTO;
    }
}
