package com.ASAF.controller;

import com.ASAF.dto.AttendanceDTO;
import com.ASAF.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    // 컨트롤러
    @PostMapping("/attendance")
    public ResponseEntity<String> attendance(@RequestParam("student_number") String student_number) {
        String resultMessage = attendanceService.check_in(student_number);
        return new ResponseEntity<>(resultMessage, HttpStatus.OK);
    }

}
