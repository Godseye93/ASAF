package com.ASAF.repository;

import com.ASAF.entity.GenerationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenerationRepository extends JpaRepository<GenerationEntity, Integer> {
}