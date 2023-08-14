package com.ASAF.repository;

import com.ASAF.entity.ClassEntity;
import com.ASAF.entity.GenerationEntity;
import com.ASAF.entity.LockerEntity;
import com.ASAF.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LockerRepository extends JpaRepository<LockerEntity, Long> {
    @Query("SELECT l FROM LockerEntity l JOIN l.class_code cc JOIN l.region_code rc JOIN l.generation_code gc WHERE cc.class_code = :class_code AND rc.region_code = :region_code AND gc.generation_code = :generation_code")
    List<LockerEntity> findByClassCodeAndRegionCodeAndGenerationCode(
            @Param("class_code") int class_code,
            @Param("region_code") int region_code,
            @Param("generation_code") int generation_code
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM LockerEntity l WHERE l.class_code = ?1 AND l.region_code = ?2 AND l.generation_code = ?3")
    void deleteByClassCodeAndRegionCodeAndGenerationCode(ClassEntity classCode, RegionEntity regionCode, GenerationEntity generationCode);

    @Query("SELECT MAX(l.locker_id) FROM LockerEntity l")
    Long findMaxLockerId();

    @Query("SELECT l FROM LockerEntity l JOIN l.class_code cc JOIN l.region_code rc JOIN l.generation_code gc JOIN l.id li WHERE cc.class_code = :class_code AND rc.region_code = :region_code AND gc.generation_code = :generation_code AND li.id = :id")
    Optional<LockerEntity> findByClassCodeAndRegionCodeANdGenerationCodeAndId(
            @Param("class_code") int class_code,
            @Param("region_code") int region_code,
            @Param("generation_code") int generation_code,
            @Param("id") int id);
}
