// AttendanceController.java
package com.ASAF.controller;

import com.ASAF.dto.AttendanceDTO;
import com.ASAF.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;



//    @PostMapping("/checkin/{id}")
//    public AttendanceDTO checkIn(@PathVariable int id) {
//        return attendanceService.updateAttendanceByMemberId(id);
//    }
//
//    @PostMapping("/checkout/{memberId}")
//    public AttendanceDTO checkOut(@PathVariable int memberId) {
//        return AttendanceDTO.toAttendanceDTO(attendanceService.attendanceCheckOut(memberId));
//    }

    @GetMapping
    public List<AttendanceDTO> getAllAttendances() {
        return attendanceService.findAll();
    }
}
