package com.ASAF.service;

import com.ASAF.dto.*;
import com.ASAF.entity.ClassInfoEntity;
import com.ASAF.repository.ClassInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassInfoService {
    private final ClassInfoRepository classInfoRepository;

    public List<ClassInfoDTO> getClassInfoByMemberId(int memberId) {
        List<ClassInfoEntity> classInfoEntities = classInfoRepository.findById_id(memberId);
        return classInfoEntities.stream()
                .map(ClassInfoDTO::toClassInfoDTO)
                .collect(Collectors.toList());
    }

    public ClassInfoService(ClassInfoRepository classInfoRepository) {
        this.classInfoRepository = classInfoRepository;
    }
}