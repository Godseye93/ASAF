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

    @ManyToOne
    @JoinColumn(name = "regionId")
    private RegionEntity region;

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
//        busEntity.setRegion(region);
        return busEntity;
    }

    public static BusEntity toUpdateBusEntity(BusDTO classDTO) {
        BusEntity classEntity = new BusEntity();
        classEntity.setBusNum(classDTO.getBusNum());
        classEntity.setLocation(classDTO.getLocation());
        classEntity.setBus_route(classDTO.getBus_route());
//        busEntity.setRegion(region);
        return classEntity;
    }
}