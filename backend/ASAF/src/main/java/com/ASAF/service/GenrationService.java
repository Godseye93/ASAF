package com.ASAF.service;

import com.ASAF.repository.GenerationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenrationService {
    private final GenerationRepository generationRepository;

}
