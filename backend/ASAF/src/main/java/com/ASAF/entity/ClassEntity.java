package com.ASAF.entity;

import com.ASAF.dto.ClassDTO;
import com.ASAF.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "class")
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int class_code;

    private String classname;

    public static ClassEntity toClassEntity(ClassDTO classDTO) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassname(classDTO.getClassname());
        return classEntity;
    }

    public static ClassEntity toUpdateClassEntity(ClassDTO classDTO) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setClass_code(classDTO.getClass_code());
        classEntity.setClassname(classDTO.getClassname());
        return classEntity;
    }
}
