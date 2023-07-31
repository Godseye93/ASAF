package com.ASAF.service;

import com.ASAF.dto.RegionDTO;
import com.ASAF.entity.RegionEntity;
import com.ASAF.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    @Autowired
    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public RegionDTO findById(int id) {
        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(id);
        if (optionalRegionEntity.isPresent()) {
            return RegionDTO.toRegionDTO(optionalRegionEntity.get());
        } else {
            return null;
        }
    }
}
