package com.ASAF.entity;

import com.ASAF.dto.BusDTO;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "bus")
public class BusEntity {

    private String region_name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int busNum;

    @Column
    private String location;

    @Column
    private String bus_route;

    public static BusEntity toBusEntity(BusDTO busDTO) {
        BusEntity busEntity = new BusEntity();
        busEntity.setLocation(busDTO.getLocation());
        busEntity.setBus_route(busDTO.getBus_route());
        busEntity.setRegion_name(busDTO.getRegion_name());
        return busEntity;
    }

    public static BusEntity toUpdateBusEntity(BusDTO busDTO) {
        BusEntity busEntity = new BusEntity();
        busEntity.setBusNum(busDTO.getBusNum());
        busEntity.setLocation(busDTO.getLocation());
        busEntity.setBus_route(busDTO.getBus_route());
        busEntity.setRegion_name(busDTO.getRegion_name());


        return busEntity;
    }
}