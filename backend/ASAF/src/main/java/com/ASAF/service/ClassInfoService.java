package com.ASAF.service;

import com.ASAF.dto.*;
import com.ASAF.entity.*;
import com.ASAF.repository.ClassInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassInfoService {
    private final ClassInfoRepository classInfoRepository;

    public ClassInfoService(ClassInfoRepository classInfoRepository) {
        this.classInfoRepository = classInfoRepository;
    }

    public List<ClassInfoDTO> getClassInfoByMemberId(int memberId) {
        List<ClassInfoEntity> classInfoEntities = classInfoRepository.findById_id(memberId);
        return classInfoEntities.stream()
                .map(ClassInfoDTO::toClassInfoDTO)
                .collect(Collectors.toList());
    }
    public List<MemberDTO> findMemberDTOsByClassRegionAndGeneration(int class_code, int region_code, int generation_code) {
        List<MemberEntity> memberEntities = classInfoRepository.findMembersByClassRegionAndGeneration(class_code, region_code, generation_code);
        return memberEntities.stream()
                .map(MemberDTO::fromMemberEntity)
                .collect(Collectors.toList());
    }

    public ClassInfoDTO saveClassInfo(ClassInfoDTO classInfoDTO) {
        ClassInfoEntity entity = ClassInfoEntity.toClassInfoEntity(classInfoDTO);
        ClassInfoEntity savedEntity = classInfoRepository.save(entity);
        return ClassInfoDTO.toClassInfoDTO(savedEntity);
    }
}