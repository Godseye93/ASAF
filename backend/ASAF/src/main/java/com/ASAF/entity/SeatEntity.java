package com.ASAF.entity;

import com.ASAF.dto.SeatDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "seat_arrange")
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seat_id;

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
    private int seat_num;

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
    
    public static SeatEntity toSeatEntity(SeatDTO seatDTO) {
        SeatEntity seatEntity = new SeatEntity();
        
        seatEntity.setSeat_num(seatDTO.getSeat_num());
        seatEntity.setName(seatDTO.getName());

        ClassEntity classEntity = new ClassEntity();
        classEntity.setClass_code(seatDTO.getClass_code());
        seatEntity.setClass_code(classEntity);

        ClassInfoEntity classInfoEntity = new ClassInfoEntity();
        classInfoEntity.setClass_num(seatDTO.getClass_num());
        seatEntity.setClass_num(classInfoEntity);

        RegionEntity regionEntity = new RegionEntity();
        regionEntity.setRegion_code(seatDTO.getRegion_code());
        seatEntity.setRegion_code(regionEntity);

        GenerationEntity generationEntity = new GenerationEntity();
        generationEntity.setGeneration_code(seatDTO.getGeneration_code());
        seatEntity.setGeneration_code(generationEntity);

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(seatDTO.getId());
        seatEntity.setId(memberEntity);
        return seatEntity;
    }
}
