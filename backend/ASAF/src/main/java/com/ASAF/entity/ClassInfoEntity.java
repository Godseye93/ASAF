package com.ASAF.entity;

import com.ASAF.dto.ClassInfoDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.ASAF.dto.MemberDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "memberId")
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

    public ClassInfoDTO toDTO() {
        ClassInfoDTO classInfoDTO = new ClassInfoDTO();
        classInfoDTO.setClass_num(getClass_num());
        classInfoDTO.setClass_code(getClass_code().getClass_code());
        classInfoDTO.setRegion_code(getRegion_code().getRegion_code());
        classInfoDTO.setGeneration_code(getGeneration_code().getGeneration_code());
        classInfoDTO.setMemberDTO(MemberDTO.toMemberDTO(getId()));
        return classInfoDTO;
    }

}