package com.ASAF.repository;

import com.ASAF.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    @Query("SELECT c FROM BookEntity c WHERE c.class_code.class_code = :class_code AND c.region_code.region_code = :region_code AND c.generation_code.generation_code = :generation_code")
    List<BookEntity> findBooksByClassRegionAndGeneration(
            @Param("class_code") int class_code,
            @Param("region_code") int region_code,
            @Param("generation_code") int generation_code);

    @Query("SELECT c FROM BookEntity c WHERE c.borrowState = true AND c.class_code.class_code = :class_code AND c.region_code.region_code = :region_code AND c.generation_code.generation_code = :generation_code")
    List<BookEntity> findBooksByClassRegionAndGenerationAndBorrowState(
            @Param("class_code") int class_code,
            @Param("region_code") int region_code,
            @Param("generation_code") int generation_code);
    List<BookEntity> findByBorrowStateTrueAndId_id(int memberId);
}
