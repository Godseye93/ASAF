package com.ASAF.entity;

import com.ASAF.dto.LockerDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "locker_arrange")
public class LockerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locker_id;

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

    @Column
    private int locker_num;

    @Column
    private String name;

    public static LockerEntity toLockerEntity(LockerDTO lockerDTO) {
        LockerEntity lockerEntity = new LockerEntity();
        lockerEntity.setLocker_id(lockerDTO.getLocker_id());
        lockerEntity.setLocker_num(lockerDTO.getLocker_num());
        lockerEntity.setName(lockerDTO.getName());
        return lockerEntity;
    }

    public static LockerEntity toUpdateLockerEntity(LockerDTO lockerDTO) {
        LockerEntity lockerEntity = new LockerEntity();
        lockerEntity.setLocker_id(lockerDTO.getLocker_id());
        lockerEntity.setLocker_num(lockerDTO.getLocker_num());
        lockerEntity.setName(lockerDTO.getName());
        return lockerEntity;
    }
}












