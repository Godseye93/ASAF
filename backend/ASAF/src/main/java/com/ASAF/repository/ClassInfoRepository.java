package com.ASAF.repository;

import com.ASAF.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ClassInfoRepository extends JpaRepository<ClassInfoEntity, Integer> {

    List<ClassInfoEntity> findById_id(int memberId);

    @Query("SELECT c.id FROM ClassInfoEntity c WHERE c.class_code.class_code = :class_code AND c.region_code.region_code = :region_code AND c.generation_code.generation_code = :generation_code")
    List<MemberEntity> findMembersByClassRegionAndGeneration(
            @Param("class_code") int class_code,
            @Param("region_code") int region_code,
            @Param("generation_code") int generation_code);

    @Transactional
    @Modifying
    @Query("DELETE FROM ClassInfoEntity ci WHERE ci.id.id = :memberId")
    void removeClassInfoByMemberId(int memberId);
}