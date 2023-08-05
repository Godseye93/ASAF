package com.ASAF.controller;

import com.ASAF.dto.MemberDTO;
import com.ASAF.dto.GenerationDTO;
import com.ASAF.service.GenerationService;
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
@RequestMapping("/generation")
public class GenerationController {
    private final GenerationService generationService;
    @GetMapping("/{id}")
    public ResponseEntity<GenerationDTO> findById(@PathVariable int id) {
        GenerationDTO generationDTO = generationService.findById(id);
        return new ResponseEntity<>(generationDTO, HttpStatus.OK);
    }
}
