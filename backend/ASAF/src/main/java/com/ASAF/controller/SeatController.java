package com.ASAF.controller;

import com.ASAF.dto.SeatDTO;
import com.ASAF.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/seat")
@RestController
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    // POST 요청으로 배치 완료
    @PostMapping("/complete")
    public ResponseEntity<SeatDTO> completeSeat(@RequestBody SeatDTO seatDTO) {
        SeatDTO savedSeatDTO = seatService.completeSeat(seatDTO);
        return ResponseEntity.ok(savedSeatDTO);
    }

    // GET 요청으로 반 별 배치 정보 가져오기
    @GetMapping("/{classCode}")
    public ResponseEntity<List<SeatDTO>> getSeatsByClass(@PathVariable int classCode) {
        List<SeatDTO> seatDTOList = seatService.getSeatsByClass(classCode);
        return ResponseEntity.ok(seatDTOList);
    }
}
