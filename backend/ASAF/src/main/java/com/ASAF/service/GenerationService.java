package com.ASAF.service;

import com.ASAF.dto.GenerationDTO;
import com.ASAF.entity.GenerationEntity;
import com.ASAF.repository.GenerationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenerationService {

    private final GenerationRepository generationRepository;

    @Autowired
    public GenerationService(GenerationRepository generationRepository) {
        this.generationRepository = generationRepository;
    }

    public GenerationDTO findById(int id) {
        Optional<GenerationEntity> optionalGenerationEntity = generationRepository.findById(id);
        if (optionalGenerationEntity.isPresent()) {
            return GenerationDTO.toGenerationDTO(optionalGenerationEntity.get());
        } else {
            return null;
        }
    }
}
