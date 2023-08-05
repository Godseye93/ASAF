package com.ASAF.controller;

import com.ASAF.dto.MemberDTO;
import com.ASAF.dto.ClassDTO;
import com.ASAF.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/class")
public class ClassController {
    private final ClassService classService;
    @GetMapping("/{id}")
    public ResponseEntity<ClassDTO> findById(@PathVariable int id) {
        ClassDTO classDTO = classService.findById(id);
        return new ResponseEntity<>(classDTO, HttpStatus.OK);
    }
}
