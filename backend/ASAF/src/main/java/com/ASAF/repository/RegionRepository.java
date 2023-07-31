package com.ASAF.repository;

import com.ASAF.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<RegionEntity, Integer>{
//    Optional<RegionEntity> findByRegionName(String region_name);
}

