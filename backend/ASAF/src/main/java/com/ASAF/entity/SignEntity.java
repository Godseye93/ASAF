package com.ASAF.entity;

import com.ASAF.dto.SignDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sign")
public class SignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sign_id;

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
    private String image_url;

    @Column
    private String name;

    @Column
    private String month;

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
}