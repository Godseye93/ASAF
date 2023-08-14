package com.ASAF.repository;

import com.ASAF.entity.SignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SignRepository extends JpaRepository<SignEntity, Long> {
    Optional<SignEntity> findByName(String name);

    @Query("SELECT s FROM SignEntity s WHERE s.class_code.class_code = :classCode AND s.region_code.region_code = :regionCode AND s.generation_code.generation_code = :generationCode AND s.month = :month")
    List<SignEntity> findByClassEntityClassCodeAndRegionEntityRegionCodeAndGenerationEntityGenerationCodeAndMonth(@Param("classCode") int classCode, @Param("regionCode") int regionCode, @Param("generationCode") int generationCode, @Param("month") String month);

    List<SignEntity> findByNameAndMonth(String name, String month);
}
