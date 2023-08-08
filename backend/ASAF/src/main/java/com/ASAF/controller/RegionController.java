package com.ASAF.controller;

import com.ASAF.dto.RegionDTO;
import com.ASAF.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/region")
public class RegionController {
    private final RegionService regionService;
    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable int id) {
        RegionDTO regionDTO = regionService.findById(id);
        return new ResponseEntity<>(regionDTO.getRegion_name(), HttpStatus.OK);
    }
}
