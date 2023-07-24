package com.ASAF.dto;

import com.ASAF.entity.RegionEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegionDTO {
    private int region_code;
    private String region_name;

    public static RegionDTO toRegionDTO(RegionEntity classEntity) {
        RegionDTO classDTO = new RegionDTO();
        classDTO.setRegion_code(classEntity.getRegion_code());
        classDTO.setRegion_name(classEntity.getRegion_name());
        return classDTO;
    }





}