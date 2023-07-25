package com.ASAF.service;

import com.ASAF.dto.RegionDTO;
import com.ASAF.entity.RegionEntity;
import com.ASAF.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    public void save(RegionDTO regionDTO) {
        RegionEntity regionEntity = RegionEntity.toRegionEntity(regionDTO);
        regionRepository.save(regionEntity);
    }
    public List<RegionDTO> findAll() {
        List<RegionEntity> regionEntityList = regionRepository.findAll();
        List<RegionDTO> regionDTOList = new ArrayList<>();
        for (RegionEntity regionEntity: regionEntityList) {
            regionDTOList.add(RegionDTO.toRegionDTO(regionEntity));
        }
        return regionDTOList;
    }

    public RegionDTO findById(Long region_code) {
        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(region_code);
        if (optionalRegionEntity.isPresent()) {
            return RegionDTO.toRegionDTO(optionalRegionEntity.get());
        } else {
            return null;
        }
    }

    public RegionDTO updateForm(String region_name) {
        Optional<RegionEntity> optionalRegionEntity = regionRepository.findByRegionName(region_name);
        if (optionalRegionEntity.isPresent()) {
            return RegionDTO.toRegionDTO((optionalRegionEntity.get()));
        } else {
            return null;
        }
    }

    public void update(RegionDTO regionDTO) { regionRepository.save(RegionEntity.toUpdateRegionEntity(regionDTO)); }

    public void deleteById(Long region_code) { regionRepository.deleteById(region_code);}

    public String regionNameCheck(String region_name) {
        Optional<RegionEntity> byRegionName = regionRepository.findByRegionName(region_name);
        if (byRegionName.isPresent()) {
            return "중복된 지역명이 존재합니다";
        } else {
            return "사용가능 한 지역명입니다.";
        }
    }
}