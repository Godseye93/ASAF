package com.ASAF.service;

import com.ASAF.dto.*;
import com.ASAF.entity.ClassInfoEntity;
import com.ASAF.repository.ClassInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassInfoService {
    private final ClassInfoRepository classInfoRepository;

    public ClassInfoDTO getClassInfoById(int Id) {
        ClassInfoEntity classInfoEntity = classInfoRepository.findById(Id).orElseThrow(() -> new ClassInfoNotFoundException("ClassInfo not found with Id: " + Id));

        ClassInfoDTO classInfoDTO = new ClassInfoDTO();
        classInfoDTO.setClass_num(classInfoEntity.getClass_num());
        classInfoDTO.setClass_code(ClassDTO.toClassDTO(classInfoEntity.getClass_code()));
        classInfoDTO.setRegion_code(RegionDTO.toRegionDTO(classInfoEntity.getRegion_code()));
        classInfoDTO.setGeneration_code(GenerationDTO.toGenerationDTO(classInfoEntity.getGeneration_code()));
        classInfoDTO.setMemberDTO(MemberDTO.toMemberDTO(classInfoEntity.getId()));

        return classInfoDTO;
    }



    public class ClassInfoNotFoundException extends RuntimeException {
        public ClassInfoNotFoundException(String message) {
            super(message);
        }
    }
}