package com.ASAF.entity;

import com.ASAF.dto.LockerDTO;
import com.ASAF.dto.LockerDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "locker_arrange")
public class LockerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locker_id;

    @ManyToOne
    @JoinColumn(name = "class_num")
    private ClassInfoEntity class_num;

    @ManyToOne
    @JoinColumn(name = "class_code")
    private ClassEntity class_code;

    @ManyToOne
    @JoinColumn(name = "region_code")
    private RegionEntity region_code;

    @ManyToOne
    @JoinColumn(name = "generation_code")
    private GenerationEntity generation_code;

    @ManyToOne
    @JoinColumn(name = "id")
    private MemberEntity id;

    @Column
    private int locker_num;

    @Column
    private String name;

    public ClassInfoEntity getClassInfoEntity() {
        return class_num;
    }
    public ClassEntity getClassEntity() {
        return class_code;
    }
    public RegionEntity getRegionEntity() {
        return region_code;
    }
    public GenerationEntity getGenerationEntity() {
        return generation_code;
    }
    public MemberEntity getMemberEntity() {
        return id;
    }

    public static LockerEntity toLockerEntity(LockerDTO lockerDTO) {
        LockerEntity lockerEntity = new LockerEntity();

        lockerEntity.setLocker_num(lockerDTO.getLocker_num());
        lockerEntity.setName(lockerDTO.getName());

        ClassEntity classEntity = new ClassEntity();
        classEntity.setClass_code(lockerDTO.getClass_code());
        lockerEntity.setClass_code(classEntity);

        ClassInfoEntity classInfoEntity = new ClassInfoEntity();
        classInfoEntity.setClass_num(lockerDTO.getClass_num());
        lockerEntity.setClass_num(classInfoEntity);

        RegionEntity regionEntity = new RegionEntity();
        regionEntity.setRegion_code(lockerDTO.getRegion_code());
        lockerEntity.setRegion_code(regionEntity);

        GenerationEntity generationEntity = new GenerationEntity();
        generationEntity.setGeneration_code(lockerDTO.getGeneration_code());
        lockerEntity.setGeneration_code(generationEntity);

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(lockerDTO.getId());
        lockerEntity.setId(memberEntity);
        return lockerEntity;
    }
}












