// AttendanceService.java
package com.ASAF.service;

import com.ASAF.dto.AttendanceDTO;
import com.ASAF.entity.AttendanceEntity;
import com.ASAF.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

//    public AttendanceDTO updateAttendanceByMemberId(int id) {
//        Optional<AttendanceEntity> attendanceEntityOptional = attendanceRepository.findByMemberId(id);
//        System.out.println(attendanceEntityOptional);
//        if (attendanceEntityOptional.isPresent()) {
//            AttendanceEntity attendanceEntity = attendanceEntityOptional.get();
//            attendanceEntity.setAttended("1");
//
//            AttendanceEntity updatedAttendanceEntity = attendanceRepository.save(attendanceEntity);
//            return AttendanceDTO.toAttendanceDTO(updatedAttendanceEntity);
//        }
//        return null;
//    }

//    public AttendanceEntity attendanceCheckIn(int id) {
//        LocalDateTime currentTime = LocalDateTime.now();
//        if (currentTime.getHour() == 8 && currentTime.getMinute() >=30 || currentTime.getHour() == 9 && currentTime.getMinute() <= 59) {
//            AttendanceEntity attendance = new AttendanceEntity();
//            attendance.setMemberId(id);
//            attendance.setCheckIn(currentTime);
//            attendance.setAttended(1);
//            return attendanceRepository.save(attendance);
//        }
//        return null;
//    }
//
//    public AttendanceEntity attendanceCheckOut(int memberId) {
//        LocalDateTime currentTime = LocalDateTime.now();
//        if (currentTime.getHour() == 18 && currentTime.getMinute() >= 0 || currentTime.getHour() == 18 && currentTime.getMinute() <= 30) {
//            AttendanceEntity attendance = attendanceRepository.findByMemberId(memberId);
//            attendance.setCheckOut(currentTime);
//            attendance.setAttended(2);
//            return attendanceRepository.save(attendance);
//        }
//        return null;
//    }

    public List<AttendanceDTO> findAll() {
        List<AttendanceEntity> attendances = attendanceRepository.findAll();
        return attendances.stream().map(AttendanceDTO::toAttendanceDTO).collect(Collectors.toList());
    }
}
