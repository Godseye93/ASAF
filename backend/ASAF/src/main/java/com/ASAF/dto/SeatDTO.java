
package com.ASAF.dto;

import com.ASAF.entity.SeatEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeatDTO {
    private Long seat_id;
    private int seat_num;
    private String name;

    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private int id;

    public SeatDTO(SeatEntity seatEntity) {
        this.seat_id = seatEntity.getSeat_id();
        this.seat_num = seatEntity.getSeat_num();
        this.name = seatEntity.getName();
        this.class_num = seatEntity.getClassInfoEntity().getClass_num();
        this.class_code = seatEntity.getClassEntity().getClass_code();
        this.region_code = seatEntity.getRegionEntity().getRegion_code();
        this.generation_code = seatEntity.getGenerationEntity().getGeneration_code();
        this.id = seatEntity.getMemberEntity().getId();
    }
}
