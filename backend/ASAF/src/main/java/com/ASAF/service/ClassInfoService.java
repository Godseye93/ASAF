package com.ASAF.service;

import com.ASAF.dto.ClassInfoDTO;
import com.ASAF.entity.ClassInfoEntity;
import com.ASAF.entity.MemberEntity;
import com.ASAF.repository.ClassInfoRepository;
import com.ASAF.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassInfoService {
    private final ClassInfoRepository classInfoRepository;
    private final MemberRepository memberRepository;

    public ClassInfoDTO getClassInfo(int class_code) {
        return classInfoRepository.findByClass_codeClass_code(class_code)
                .map(ClassInfoEntity::toDTO)
                .orElse(null);
    }

    public List<ClassInfoDTO> findAll() {
        return classInfoRepository.findAll().stream()
                .map(ClassInfoEntity::toDTO)
                .collect(Collectors.toList());
    }
    public ClassInfoDTO getClassInfoByMemberId(int id) {
        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found for ID: " + id));
        return classInfoRepository.findByMember(member)
                .map(ClassInfoEntity::toDTO)
                .orElseThrow(() -> new RuntimeException("ClassInfo not found for the given member ID"));
    }
}
