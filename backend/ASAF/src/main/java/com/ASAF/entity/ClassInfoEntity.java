package com.ASAF.entity;

import com.ASAF.dto.ClassInfoDTO;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ClassInfoEntity")
public class ClassInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int class_num;


    @ManyToOne
    @JoinColumn(name = "classCode")
    private ClassEntity class_code;

    @ManyToOne
    private RegionEntity region_code;

    @ManyToOne
    private GenerationEntity generation_code;

    @ManyToOne
    private MemberEntity id;


    public static ClassInfoEntity toClassInfoEntity(ClassInfoDTO classInfoDTO) {
        ClassInfoEntity classInfoEntity = new ClassInfoEntity();
        return classInfoEntity;
    }

    public static ClassInfoEntity toUpdateClassInfoEntity(ClassInfoDTO classInfoDTO) {
        ClassInfoEntity classInfoEntity = new ClassInfoEntity();
        classInfoEntity.setClass_num(classInfoDTO.getClass_num());
        return classInfoEntity;
    }
}