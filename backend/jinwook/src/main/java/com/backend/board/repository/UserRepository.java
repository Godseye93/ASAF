package com.backend.board.repository;

import com.backend.board.model.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<MemberDTO, Long> {
    MemberDTO findByUsername(String username);
}