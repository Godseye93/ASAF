package com.ASAF.repository;

import com.ASAF.entity.ClassEntity;
import com.ASAF.entity.GenerationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {
}
