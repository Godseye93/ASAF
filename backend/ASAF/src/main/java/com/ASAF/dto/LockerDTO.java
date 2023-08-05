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
    private int locker_num;
    private String name;

    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private int id;

    public LockerDTO(LockerEntity lockerEntity) {
        this.locker_id = lockerEntity.getLocker_id();
        this.locker_num = lockerEntity.getLocker_num();
        this.name = lockerEntity.getName();
        this.class_num = lockerEntity.getClassInfoEntity().getClass_num();
        this.class_code = lockerEntity.getClassEntity().getClass_code();
        this.region_code = lockerEntity.getRegionEntity().getRegion_code();
        this.generation_code = lockerEntity.getGenerationEntity().getGeneration_code();
        this.id = lockerEntity.getMemberEntity().getId();
    }
}
