package com.ASAF.repository;

import com.ASAF.entity.ImageEntity;
import com.ASAF.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, PostEntity> {
    List<ImageEntity> findByPost(PostEntity postEntity);

    List<ImageEntity> findByPostId(Long postId);
}
