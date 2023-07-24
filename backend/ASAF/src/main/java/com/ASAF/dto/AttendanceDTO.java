package com.ASAF.dto;

import com.ASAF.entity.AttendanceEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttendanceDTO {
    private Long attendanceId;
    private MemberDTO memberDTO;
    private String attended;
    private String entryTime;
    private String exitTime;

    // AttendanceEntity 타입의 하나의 매개변수 attendanceEntity를 입력으로 받고 AttendanceDTO 객체를 반환합니다.
    // 이 메서드의 목적은 AttendanceEntity 객체의 정보를 가져와 AttendanceDTO 객체로 변환하는 것입니다.
    public static AttendanceDTO toAttendanceDTO(AttendanceEntity attendanceEntity) {
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setAttendanceId(attendanceEntity.getAttendanceId());
        attendanceDTO.setMemberDTO(MemberDTO.toMemberDTO(attendanceEntity.getMember()));
        attendanceDTO.setAttended(attendanceEntity.getAttended());
        attendanceDTO.setEntryTime(attendanceEntity.getEntryTime());
        attendanceDTO.setExitTime(attendanceEntity.getExitTime());
        return attendanceDTO;
    }
}
