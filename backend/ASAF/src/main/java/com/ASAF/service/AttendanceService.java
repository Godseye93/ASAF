// AttendanceService.java
package com.ASAF.service;

import com.ASAF.dto.AttendanceDTO;
import com.ASAF.entity.AttendanceEntity;
import com.ASAF.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public AttendanceDTO CheckIn(int id) {
        Optional<AttendanceEntity> attendanceEntityOptional = attendanceRepository.findByMemberId(id);
        LocalTime currentTime = LocalTime .now();
        if (attendanceEntityOptional.isPresent()) {
            AttendanceEntity attendanceEntity = attendanceEntityOptional.get();
            attendanceEntity.setEntryTime(Time.valueOf(currentTime));
            attendanceEntity.setAttended("입실");
            AttendanceEntity updatedAttendanceEntity = attendanceRepository.save(attendanceEntity);
            return AttendanceDTO.toAttendanceDTO(updatedAttendanceEntity);
        }
        return null;
    }

    public AttendanceDTO CheckOut(int id) {
        Optional<AttendanceEntity> attendanceEntityOptional = attendanceRepository.findByMemberId(id);
        LocalTime currentTime = LocalTime .now();
        if (attendanceEntityOptional.isPresent()) {
            AttendanceEntity attendanceEntity = attendanceEntityOptional.get();
            attendanceEntity.setExitTime(Time.valueOf(currentTime));
            attendanceEntity.setAttended("퇴실");
            AttendanceEntity updatedAttendanceEntity = attendanceRepository.save(attendanceEntity);
            return AttendanceDTO.toAttendanceDTO(updatedAttendanceEntity);
        }
        return null;
    }




    public List<AttendanceDTO> findAll() {
        List<AttendanceEntity> attendances = attendanceRepository.findAll();
        return attendances.stream().map(AttendanceDTO::toAttendanceDTO).collect(Collectors.toList());
    }


    public AttendanceDTO findById(int id) {
        Optional<AttendanceEntity> optionalAttendanceEntity = attendanceRepository.findByMemberId(id);
        if (optionalAttendanceEntity.isPresent()) {
            AttendanceEntity attendanceEntity = optionalAttendanceEntity.get();
            return AttendanceDTO.toAttendanceDTO(attendanceEntity);
        } else {
            return null;
        }
    }
}



// public AttendanceDTO ssafyCheckIn(int id) {
//        Optional<AttendanceEntity> attendanceEntityOptional = attendanceRepository.findByMemberId(id);
//        LocalTime currentTime = LocalTime.now();
//        if (currentTime.getHour() == 8 && currentTime.getMinute() >=30 || currentTime.getHour() == 9 && currentTime.getMinute() <= 59 && attendanceEntityOptional.isPresent()) {
//            AttendanceEntity attendanceEntity = attendanceEntityOptional.get();
//            attendanceEntity.setEntryTime(Time.valueOf(currentTime));
//            attendanceEntity.setAttended("1");
//            AttendanceEntity updatedssafyCheckIn = attendanceRepository.save(attendanceEntity);
//            return AttendanceDTO.toAttendanceDTO(updatedssafyCheckIn);
//        }
//        return null;
//    }
