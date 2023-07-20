package com.backend.board.service;

import com.backend.board.model.MemberDTO;

import java.util.List;

public interface MemberService {

    void registerUser(MemberDTO memberDTO);

    MemberDTO login(MemberDTO memberDTO);

    List<MemberDTO> findAllMembers();

    MemberDTO findMemberById(Long id);

    void updateMember(MemberDTO memberDTO);

    void deleteMemberById(Long id);

    String emailCheck(String memberEmail);
}