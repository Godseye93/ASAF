// AttendanceController.java
package com.ASAF.controller;

import com.ASAF.dto.AttendanceDTO;
import com.ASAF.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/checkin/{id}")
    public AttendanceDTO checkIn(@PathVariable int id) {
        return attendanceService.CheckIn(id);
    }

    @PostMapping("/checkout/{id}")
    public AttendanceDTO checkOut(@PathVariable int id) {
        return attendanceService.CheckOut(id);
    }

    @GetMapping
    public List<AttendanceDTO> getAllAttendances() {
        return attendanceService.findAll();
    }

    @GetMapping("/{id}")
    public AttendanceDTO getAttendance(@PathVariable int id) {
        return attendanceService.findById(id);
    }
}
