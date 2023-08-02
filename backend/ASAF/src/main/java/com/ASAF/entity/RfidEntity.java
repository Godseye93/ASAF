package com.ASAF.entity;

import com.ASAF.dto.RfidDTO;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "RFID")
public class RfidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rfid_id;

    private String rfidNumber;

    public static RfidEntity toRfidEntity(RfidDTO rfidDTO) {
        RfidEntity rfidEntity = new RfidEntity();
        rfidEntity.setRfidNumber(rfidDTO.getRfidNumber());
        return rfidEntity;
    }

    public static RfidEntity toUpdateRfidEntity(RfidDTO rfidDTO) {
        RfidEntity rfidEntity = new RfidEntity();
        rfidEntity.setRfid_id(rfidDTO.getRfid_id());
        rfidEntity.setRfidNumber(rfidDTO.getRfidNumber());
        return rfidEntity;
    }
}