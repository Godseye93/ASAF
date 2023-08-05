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

    public static RegionDTO toRegionDTO(RegionEntity regionEntity) {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegion_code(regionEntity.getRegion_code());
        regionDTO.setRegion_name(regionEntity.getRegion_name());
        return regionDTO;
    }
}