package com.ASAF.controller;

import com.ASAF.dto.BusDTO;
import com.ASAF.entity.RegionEntity;
import com.ASAF.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bus")
public class BusController {

    @Autowired
    private BusService busService;


    @GetMapping
    public List<BusDTO> getAllBus() {
        return busService.findAll();
    }

    // 2. 특정 지역 버스들을 조회
    @GetMapping("/{region_code}")
    public BusDTO getBusByRegion(@PathVariable("region_code") int region_code) {
        return busService.findByRegion(region_code);
    }



}
