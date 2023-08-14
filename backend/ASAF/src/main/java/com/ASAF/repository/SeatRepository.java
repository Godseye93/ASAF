package com.ASAF.repository;

import com.ASAF.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
    @Query("SELECT s FROM SeatEntity s JOIN s.class_code cc JOIN s.region_code rc JOIN s.generation_code gc WHERE cc.class_code = :class_code AND rc.region_code = :region_code AND gc.generation_code = :generation_code")
    List<SeatEntity> findByClassCodeAndRegionCodeAndGenerationCode(
            @Param("class_code") int class_code,
            @Param("region_code") int region_code,
            @Param("generation_code") int generation_code);

    @Transactional
    @Modifying
    @Query("DELETE FROM SeatEntity s WHERE s.class_code = ?1 AND s.region_code = ?2 AND s.generation_code = ?3")
    void deleteByClassCodeAndRegionCodeAndGenerationCode(ClassEntity classCode, RegionEntity regionCode, GenerationEntity generationCode);

    @Query("SELECT s FROM SeatEntity s JOIN s.class_code cc JOIN s.region_code rc JOIN s.generation_code gc JOIN s.id si WHERE cc.class_code = :class_code AND rc.region_code = :region_code AND gc.generation_code = :generation_code AND si.id = :id")
    Optional<SeatEntity> findByClassCodeAndRegionCodeAndGenerationCodeAndId(
            @Param("class_code") int class_code,
            @Param("region_code") int region_code,
            @Param("generation_code") int generation_code,
            @Param("id") int id);
}
