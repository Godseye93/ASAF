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
    private int doc_id;
    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private String user_id;
    private String seat_assign;

    public SeatDTO(SeatEntity seatEntity) {
        this.seat_id = seatEntity.getSeat_id();
        this.seat_assign = seatEntity.getSeat_assign();
    }

    public static SeatDTO toSeatDTO(SeatEntity seatEntity) {
        SeatDTO seatDTO = new SeatDTO();
        seatDTO.setSeat_id(seatEntity.getSeat_id());
        seatDTO.setSeat_assign(seatEntity.getSeat_assign());
        return seatDTO;
    }
}
