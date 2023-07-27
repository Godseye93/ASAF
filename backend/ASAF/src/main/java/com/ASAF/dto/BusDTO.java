package com.ASAF.dto;

import com.ASAF.entity.BusEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusDTO {
    private int busNum;
    private String location;
    private String bus_route;
//    private int regionId;

    public static BusDTO BusDTO(BusEntity busEntity) {
        BusDTO busDTO = new BusDTO();
        busDTO.setLocation(busEntity.getLocation());
        busDTO.setBus_route(busEntity.getBus_route());
        return busDTO;
    }

    public static BusDTO toBusDTO(BusEntity busEntity) {
        BusDTO busDTO = new BusDTO();
        busDTO.setBusNum(busEntity.getBusNum());
        busDTO.setLocation(busEntity.getLocation());
        busDTO.setBus_route(busEntity.getBus_route());
        return busDTO;
    }
}