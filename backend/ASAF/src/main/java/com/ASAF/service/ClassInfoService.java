package com.ASAF.service;

import com.ASAF.dto.ClassInfoDTO;
import com.ASAF.entity.ClassInfoEntity;
import com.ASAF.repository.ClassInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassInfoService {
    private final ClassInfoRepository classInfoRepository;

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

    public ClassInfoDTO getClassInfoById(int id) {
        return classInfoRepository.findById(id)
                .map(ClassInfoEntity::toDTO)
                .orElseThrow(() -> new RuntimeException("ClassInfo not found for id: " + id));
    }
}
