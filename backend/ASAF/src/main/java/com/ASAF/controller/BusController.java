package com.ASAF.controller;

import com.ASAF.dto.BusDTO;
import com.ASAF.dto.BusDTO;
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

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody BusDTO busDTO) {
        busService.save(busDTO);
        return new ResponseEntity<>("버스 등록 성공", HttpStatus.OK);
    }

    @PostMapping("/{busNum}/{location}")
    public BusDTO update(@PathVariable int busNum, @PathVariable String location) {
        return busService.update(busNum, location);
    }



}
