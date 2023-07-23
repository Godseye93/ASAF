package com.ASAF.entity;

import com.ASAF.dto.AttendanceDTO;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attendance_table")
public class AttendanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @OneToOne
    @JoinColumn(name = "student_number", referencedColumnName = "student_number")
    private MemberEntity member;

    @Column(nullable = false)
    private Boolean attended = false;

    @Column
    private String entryTime;

    @Column
    private String exitTime;

    public static AttendanceEntity toAttendanceEntity(AttendanceDTO attendanceDTO) {
        AttendanceEntity attendanceEntity = new AttendanceEntity();
        attendanceEntity.setAttended(attendanceDTO.getAttended());
        attendanceEntity.setEntryTime(attendanceDTO.getEntryTime());
        attendanceEntity.setExitTime(attendanceDTO.getExitTime());
        // 멤버 엔티티 세팅은 더 정교한 매핑이 필요하므로, 별도로 추가하세요.
        return attendanceEntity;
    }

    public static AttendanceEntity toUpdateAttendanceEntity(AttendanceDTO attendanceDTO) {
        AttendanceEntity attendanceEntity = new AttendanceEntity();
        attendanceEntity.setAttendanceId(attendanceDTO.getAttendanceId());
        attendanceEntity.setAttended(attendanceDTO.getAttended());
        attendanceEntity.setEntryTime(attendanceDTO.getEntryTime());
        attendanceEntity.setExitTime(attendanceDTO.getExitTime());
        // 멤버 엔티티 세팅은 더 정교한 매핑이 필요하므로, 별도로 추가하세요.
        return attendanceEntity;
    }
}
