package com.ASAF.controller;

import com.ASAF.dto.SeatDTO;
import com.ASAF.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RequestMapping("/seat")
@RestController
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // 자리 배치 완료
    @PostMapping("/complete")
    public SeatDTO completeSeat(@RequestBody SeatDTO seatDTO) {
        return seatService.completeSeat(seatDTO);
    }

    @GetMapping
    public List<SeatDTO> getAllSeats() {
        List<SeatDTO> seats = seatService.getAllSeats();
        seats.sort(Comparator.comparingInt(SeatDTO::getSeat_num));
        return seats;
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
