package com.ASAF.repository;

import com.ASAF.entity.RegionEntity;
import com.ASAF.entity.RfidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RfidRepository extends JpaRepository<RfidEntity, Integer> {
    Optional<RfidEntity> findByRfidNumber(String RfidNumber);
}

