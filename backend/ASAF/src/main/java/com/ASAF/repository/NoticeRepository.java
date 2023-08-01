package com.ASAF.repository;

import com.ASAF.entity.MemberEntity;
import com.ASAF.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {
    // Optional<MemberEntity> findByMemberEmail(String memberEmail);
}


