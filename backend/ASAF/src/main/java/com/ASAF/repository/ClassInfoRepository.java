package com.ASAF.repository;

import com.ASAF.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassInfoRepository extends JpaRepository<ClassInfoEntity, Integer> {

    List<ClassInfoEntity> findById_id(int memberId);
}