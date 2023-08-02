package com.ASAF.repository;

import com.ASAF.entity.LockerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LockerRepository extends JpaRepository<LockerEntity, Long> {
    @Query("SELECT l FROM LockerEntity l JOIN l.class_code cc JOIN l.region_code rc JOIN l.generation_code gc WHERE cc.class_code = :class_code AND rc.region_code = :region_code AND gc.generation_code = :generation_code")
    List<LockerEntity> findByClassCodeAndRegionCodeAndGenerationCode(
            @Param("class_code") int class_code,
            @Param("region_code") int region_code,
            @Param("generation_code") int generation_code
    );
}
