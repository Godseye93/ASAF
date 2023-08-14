package com.ASAF.controller;

import com.ASAF.dto.SeatDTO;
import com.ASAF.entity.SeatEntity;
import com.ASAF.service.SeatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequestMapping("/seat")
@RestController
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;
    @Autowired
    private final ObjectMapper objectMapper;

    // 자리 배치 완료
    @PostMapping("/complete")
    public ResponseEntity<Boolean> completeSeats(@RequestBody JsonNode jsonNode) {
        System.out.println(jsonNode);
        List<SeatDTO> seatDTOList = new ArrayList<>();

        try {
            if (jsonNode.isArray()) {
                seatDTOList = objectMapper.convertValue(jsonNode, new TypeReference<List<SeatDTO>>() {});
            } else {
                SeatDTO seatDTO = objectMapper.treeToValue(jsonNode, SeatDTO.class);
                seatDTOList.add(seatDTO);
            }
            seatService.completeSeats(seatDTOList);

            return ResponseEntity.ok(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @GetMapping
    public List<SeatDTO> getAllSeats() {
        List<SeatDTO> seats = seatService.getAllSeats();
        seats.sort(Comparator.comparingInt(SeatDTO::getSeat_num));
        return seats;
    }

    @GetMapping("/user")
    public ResponseEntity<SeatDTO> getSeatByUser(@RequestParam int class_code,
                                                 @RequestParam int region_code,
                                                 @RequestParam int generation_code,
                                                 @RequestParam int id) {
        try {
            SeatDTO seatDTO = seatService.getSeatByUser(class_code, region_code, generation_code, id);
            return ResponseEntity.ok(seatDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/classCodes")
    public List<SeatDTO> getSeatsByCodes(@RequestParam("class_code") int class_code,
                                         @RequestParam("region_code") int region_code,
                                         @RequestParam("generation_code") int generation_code) {
        List<SeatDTO> seats = seatService.getSeatsByCodes(class_code, region_code, generation_code);
        seats.sort(Comparator.comparingInt(SeatDTO::getSeat_num));
        return seats;
    }
}
