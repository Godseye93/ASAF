package com.ASAF.controller;

import com.ASAF.dto.LockerDTO;
import com.ASAF.dto.SeatDTO;
import com.ASAF.service.LockerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequestMapping("/locker")
@RestController
@RequiredArgsConstructor
public class LockerController {

    private final LockerService lockerService;
    @Autowired
    private final ObjectMapper objectMapper;

    @PostMapping("/complete")
    public ResponseEntity<Boolean> completeLockers(@RequestBody JsonNode jsonNode) {
        System.out.println(jsonNode);
        List<LockerDTO> lockerDTOList = new ArrayList<>();

        try {
            if (jsonNode.isArray()) {
                lockerDTOList = objectMapper.convertValue(jsonNode, new TypeReference<List<LockerDTO>>() {});
            } else {
                LockerDTO lockerDTO = objectMapper.treeToValue(jsonNode, LockerDTO.class);
                lockerDTOList.add(lockerDTO);
            }
            lockerService.completeLockers(lockerDTOList);

            return ResponseEntity.ok(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @GetMapping
    public List<LockerDTO> getAllLockers() {
        return lockerService.getAllLockers();
    }

    @GetMapping("/user")
    public ResponseEntity<LockerDTO> getLockerByUser(@RequestParam int class_code,
                                                     @RequestParam int region_code,
                                                     @RequestParam int generation_code,
                                                     @RequestParam int id) {
        try {
            LockerDTO lockerDTO = lockerService.getLockerByUser(class_code, region_code, generation_code, id);
            return ResponseEntity.ok(lockerDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/classCodes")
    public List<LockerDTO> getLockersByCodes(@RequestParam("class_code") int class_code,
                                             @RequestParam("region_code") int region_code,
                                             @RequestParam("generation_code") int generation_code) {
        List<LockerDTO> lockers = lockerService.getLockersByCodes(class_code, region_code, generation_code);
        lockers.sort(Comparator.comparingInt(LockerDTO::getLocker_num));
        return lockers;
    }
}
