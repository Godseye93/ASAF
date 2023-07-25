package com.ASAF.entity;

import com.ASAF.dto.SeatDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "seat_arrange")
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seat_id;

    @ManyToOne
    @JoinColumn(name = "doc_id")
    private DocumentEntity doc_id;

    @ManyToOne
    @JoinColumn(name = "class_num")
    private DocumentEntity class_num;

    @ManyToOne
    @JoinColumn(name = "class_code")
    private DocumentEntity class_code;

    @ManyToOne
    @JoinColumn(name = "region_code")
    private DocumentEntity region_code;

    @ManyToOne
    @JoinColumn(name = "generation_code")
    private DocumentEntity generation_code;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DocumentEntity id;

    @Column(columnDefinition = "json")
    private String seat_assign;

    public static SeatEntity toSeatEntity(SeatDTO seatDTO) {
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.setSeat_id(seatDTO.getSeat_id());
        seatEntity.setSeat_assign(seatDTO.getSeat_assign());
        return seatEntity;
    }

    public static SeatEntity toUpdateSeatEntity(SeatDTO seatDTO) {
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.setSeat_id(seatDTO.getSeat_id());
        seatEntity.setSeat_assign(seatDTO.getSeat_assign());
        return seatEntity;
    }
}
