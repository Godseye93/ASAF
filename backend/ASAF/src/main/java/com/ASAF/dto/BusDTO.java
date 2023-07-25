package com.ASAF.dto;

import com.ASAF.entity.BusEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusDTO {
    private int bus_num;
    private String location;
    private String bus_route;

    public static BusDTO toBusDTO(BusEntity classEntity) {
        BusDTO classDTO = new BusDTO();
        classDTO.setBus_num(classEntity.getBus_num());
        classDTO.setLocation(classEntity.getLocation());
        classDTO.setBus_route(classEntity.getBus_route());
        return classDTO;
    }
}