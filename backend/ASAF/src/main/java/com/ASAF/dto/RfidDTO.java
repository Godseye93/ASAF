package com.ASAF.dto;

import com.ASAF.entity.RfidEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RfidDTO {
    private int rfid_id;
    private String rfidNumber;

    public static RfidDTO toRfidDTO(RfidEntity rfidEntity) {
        RfidDTO rfidDTO = new RfidDTO();
        rfidDTO.setRfid_id(rfidEntity.getRfid_id());
        rfidDTO.setRfidNumber(rfidEntity.getRfidNumber());
        return rfidDTO;
    }
}
