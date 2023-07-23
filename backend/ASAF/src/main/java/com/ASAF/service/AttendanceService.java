package com.ASAF.service;

import com.ASAF.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
//    public void save(AttendanceDTO attendanceDTO) {
//        AttendanceEntity attendanceEntity = AttendanceEntity.toAttendanceEntity(attendanceDTO);
//        attendanceRepository.save(attendanceEntity);
//    }
}
