package com.ASAF.entity;

import com.ASAF.dto.RegionDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "region")
public class RegionEntity {

    @OneToMany(mappedBy = "region_code", cascade = CascadeType.ALL)
    private List<ClassInfoEntity> classInfoEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<BusEntity> busEntityList = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int region_code;

    private String region_name;

    public static RegionEntity toRegionEntity(RegionDTO regionDTO) {
        RegionEntity regionEntity = new RegionEntity();
        regionEntity.setRegion_name(regionDTO.getRegion_name());
        return regionEntity;
    }

    public static RegionEntity toUpdateRegionEntity(RegionDTO classDTO) {
        RegionEntity classEntity = new RegionEntity();
        classEntity.setRegion_code(classDTO.getRegion_code());
        classEntity.setRegion_name(classDTO.getRegion_name());
        return classEntity;
    }
}