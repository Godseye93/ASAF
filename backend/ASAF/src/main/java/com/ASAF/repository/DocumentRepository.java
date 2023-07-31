package com.ASAF.repository;

import com.ASAF.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    @Query("SELECT d FROM DocumentEntity d")
    List<DocumentEntity> findAllOrderByCreatedAtDesc();
}
