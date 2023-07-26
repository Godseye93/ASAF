package com.ASAF.entity;

import com.ASAF.dto.ClassDTO;
import com.ASAF.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "class")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int class_code;

    @Column
    private String classname;

    @OneToMany(mappedBy = "class_code", cascade = CascadeType.ALL)
    private List<ClassInfoEntity> classInfoEntityList = new ArrayList<>();

    public static ClassEntity toClassEntity(ClassDTO classDTO) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassname(classDTO.getClassname());
        classEntity.setClass_code(classDTO.getClass_code());
        return classEntity;
    }

    public static ClassEntity toUpdateClassEntity(ClassDTO classDTO) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setClass_code(classDTO.getClass_code());
        classEntity.setClassname(classDTO.getClassname());
        return classEntity;
    }
}
