package com.ASAF.controller;

import com.ASAF.dto.LockerDTO;
import com.ASAF.service.LockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
