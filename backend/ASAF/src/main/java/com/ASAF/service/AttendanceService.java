package com.ASAF.service;

import com.ASAF.dto.AttendanceDTO;
import com.ASAF.entity.AttendanceEntity;
import com.ASAF.entity.MemberEntity;
import com.ASAF.repository.AttendanceRepository;
import com.ASAF.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    public String check_in(String student_number) {

        return "입실하였습니다.";
    }


    public void save(AttendanceDTO attendanceDTO) {
        AttendanceEntity attendanceEntity = AttendanceEntity.toAttendanceEntity(attendanceDTO);
        attendanceRepository.save(attendanceEntity);
    }
}
