package com.ASAF.repository;

import com.ASAF.entity.SignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignRepository extends JpaRepository<SignEntity, Long> {
    Optional<SignEntity> findByName(String name);
}
