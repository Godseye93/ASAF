package com.ASAF.controller;

import com.ASAF.dto.LockerDTO;
import com.ASAF.service.LockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RequestMapping("/locker")
@RestController
@RequiredArgsConstructor
public class LockerController {

    private final LockerService lockerService;

    @PostMapping("/complete")
    public LockerDTO completeLocker(@RequestBody LockerDTO lockerDTO) {
        return lockerService.completeLocker(lockerDTO);
    }

    @GetMapping
    public List<LockerDTO> getAllLockers() {
        return lockerService.getAllLockers();
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
