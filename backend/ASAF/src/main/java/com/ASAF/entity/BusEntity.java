package com.ASAF.entity;

import com.ASAF.dto.BusDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "bus")
public class BusEntity {

    @ManyToOne
    @JoinColumn(name = "region_code")
    private RegionEntity region_code;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bus_num;

    private String location;

    private String bus_route;

    public static BusEntity toBusEntity(BusDTO busDTO) {
        BusEntity busEntity = new BusEntity();
        busEntity.setLocation(busDTO.getLocation());
        return busEntity;
    }

    public static BusEntity toUpdateBusEntity(BusDTO classDTO) {
        BusEntity classEntity = new BusEntity();
        classEntity.setBus_num(classDTO.getBus_num());
        classEntity.setLocation(classDTO.getLocation());
        classEntity.setBus_route(classDTO.getBus_route());
        return classEntity;
    }
}