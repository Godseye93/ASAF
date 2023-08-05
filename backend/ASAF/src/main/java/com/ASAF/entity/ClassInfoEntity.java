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

        ClassEntity classEntity = new ClassEntity();
        classEntity.setClass_code(classInfoDTO.getClass_code());
        classInfoEntity.setClass_code(classEntity);

        RegionEntity regionEntity = new RegionEntity();
        regionEntity.setRegion_code(classInfoDTO.getRegion_code());
        classInfoEntity.setRegion_code(regionEntity);

        GenerationEntity generationEntity = new GenerationEntity();
        generationEntity.setGeneration_code(classInfoDTO.getGeneration_code());
        classInfoEntity.setGeneration_code(generationEntity);

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(classInfoDTO.getId());
        classInfoEntity.setId(memberEntity);

        return classInfoEntity;
    }

    public static ClassInfoEntity toUpdateClassInfoEntity(ClassInfoDTO classInfoDTO) {
        ClassInfoEntity classInfoEntity = new ClassInfoEntity();
        classInfoEntity.setClass_num(classInfoDTO.getClass_num());
        return classInfoEntity;
    }
}