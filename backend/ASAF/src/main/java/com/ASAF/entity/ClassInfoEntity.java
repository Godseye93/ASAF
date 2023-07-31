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
    @JoinColumn(name = "class_code")
    private ClassEntity class_code;

    @ManyToOne
    @JoinColumn(name="region_code")
    private RegionEntity region_code;

    @ManyToOne
    @JoinColumn(name="generation_code")
    private GenerationEntity generation_code;

    @ManyToOne
    @JoinColumn(name="id")
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