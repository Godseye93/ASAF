package com.ASAF.repository;

import com.ASAF.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
    @Query("SELECT s FROM SeatEntity s JOIN s.class_code cc JOIN s.region_code rc JOIN s.generation_code gc WHERE cc.class_code = :class_code AND rc.region_code = :region_code AND gc.generation_code = :generation_code")
    List<SeatEntity> findByClassCodeAndRegionCodeAndGenerationCode(
            @Param("class_code") int class_code,
            @Param("region_code") int region_code,
            @Param("generation_code") int generation_code);

}
