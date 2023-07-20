package com.backend.board.service;

import com.backend.board.model.MemberDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {

    private final Map<Long, MemberDTO> members = new HashMap<>();
    private long memberIdSequence = 1L;

    @Override
    public void registerUser(MemberDTO memberDTO) {
        long newMemberId = memberIdSequence++;
        memberDTO.setId(newMemberId);
        members.put(newMemberId, memberDTO);
    }

    @Override
    public MemberDTO login(MemberDTO memberDTO) {
        for (MemberDTO member : members.values()) {
            if (member.getMemberEmail().equals(memberDTO.getMemberEmail()) &&
                    member.getPassword().equals(memberDTO.getPassword())) {
                return member;
            }
        }
        return null;
    }

    @Override
    public List<MemberDTO> findAllMembers() {
        return new ArrayList<>(members.values());
    }

    @Override
    public MemberDTO findMemberById(Long id) {
        return members.get(id);
    }

    @Override
    public void updateMember(MemberDTO memberDTO) {
        members.put(memberDTO.getId(), memberDTO);
    }

    @Override
    public void deleteMemberById(Long id) {
        members.remove(id);
    }
    @Override
    public String emailCheck(String memberEmail) {
        for (MemberDTO member : members.values()) {
            if (member.getMemberEmail().equals(memberEmail)) {
                return "duplicate";
            }
        }
        return "available";
    }
}
