package com.ASAF.entity;

import com.ASAF.dto.ClassInfoDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private ClassEntity class_code;

    @ManyToOne
    private RegionEntity region_code;

    @ManyToOne
    private GenerationEntity generation_code;

    @OneToMany(mappedBy = "classInfo", cascade = CascadeType.ALL)
    private List<MemberEntity> user_id = new ArrayList<>();

    public static ClassInfoEntity toClassInfoEntity(ClassInfoDTO classInfoDTO) {
        ClassInfoEntity classInfoEntity = new ClassInfoEntity();
        // 멤버 엔티티 세팅은 더 정교한 매핑이 필요하므로, 별도로 추가하세요.
        return classInfoEntity;
    }

    public static ClassInfoEntity toUpdateClassInfoEntity(ClassInfoDTO classInfoDTO) {
        ClassInfoEntity classInfoEntity = new ClassInfoEntity();
        classInfoEntity.setClass_num(classInfoDTO.getClass_num());
        // 멤버 엔티티 세팅은 더 정교한 매핑이 필요하므로, 별도로 추가하세요.
        return classInfoEntity;
    }

}
