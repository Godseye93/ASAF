package com.ASAF.repository;

import com.ASAF.entity.ClassInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassInfoRepository extends JpaRepository<ClassInfoEntity, Integer> {

    Optional<ClassInfoEntity> findById(int Id);

}
