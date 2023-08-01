package com.ASAF.dto;

import com.ASAF.entity.LockerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LockerDTO {
    private Long locker_id;
    private int doc_id;
    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private int id;
    private int locker_num;
    private String name;

    public LockerDTO(LockerEntity lockerEntity) {
        this.locker_id = lockerEntity.getLocker_id();
        this.locker_num = lockerEntity.getLocker_num();
        this.name = lockerEntity.getName();
    }

    public static LockerDTO toLockerDTO(LockerEntity lockerEntity) {
        LockerDTO lockerDTO = new LockerDTO();
        lockerDTO.setLocker_id(lockerEntity.getLocker_id());
        lockerDTO.setLocker_num(lockerEntity.getLocker_num());
        lockerDTO.setName(lockerEntity.getName());
        return lockerDTO;
    }
}
