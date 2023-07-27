package com.ASAF.repository;

import com.ASAF.entity.ClassInfoEntity;
import com.ASAF.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClassInfoRepository extends JpaRepository<ClassInfoEntity, Integer> {

    @Query("SELECT c FROM ClassInfoEntity c WHERE c.class_code.class_code = ?1")
    Optional<ClassInfoEntity> findByClass_codeClass_code(int classCode);
    Optional<ClassInfoEntity> findByMemberId(MemberEntity id);
}
