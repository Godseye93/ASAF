package com.ASAF.service;

import com.ASAF.dto.*;
import com.ASAF.entity.*;
import com.ASAF.repository.ClassInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public List<MemberDTO> findMemberDTOsByClassRegionAndGenerationpro(int class_code, int region_code, int generation_code) {
        List<MemberEntity> memberEntities = classInfoRepository.findMembersByClassRegionAndGeneration(class_code, region_code, generation_code);
        return memberEntities.stream()
                .map(MemberDTO::fromMemberEntity)
                .filter(memberDTO -> !memberDTO.getAuthority().equals("프로"))
                .collect(Collectors.toList());
    }

    public ClassInfoDTO saveClassInfo(ClassInfoDTO classInfoDTO) {
        ClassInfoEntity entity = ClassInfoEntity.toClassInfoEntity(classInfoDTO);
        ClassInfoEntity savedEntity = classInfoRepository.save(entity);
        return ClassInfoDTO.toClassInfoDTO(savedEntity);
    }

    public ClassInfoDTO updateClassInfo(int class_num, ClassInfoDTO classInfoDTO) {
        Optional<ClassInfoEntity> classInfoEntityOptional = classInfoRepository.findById(class_num);

        if (classInfoEntityOptional.isPresent()) {
            ClassInfoEntity classInfoEntity = classInfoEntityOptional.get();

            // 필드 업데이트
            ClassEntity classEntity = new ClassEntity();
            classEntity.setClass_code(classInfoDTO.getClass_code());
            classInfoEntity.setClass_code(classEntity);

            RegionEntity regionEntity = new RegionEntity();
            regionEntity.setRegion_code(classInfoDTO.getRegion_code());
            classInfoEntity.setRegion_code(regionEntity);

            GenerationEntity generationEntity = new GenerationEntity();
            generationEntity.setGeneration_code(classInfoDTO.getGeneration_code());
            classInfoEntity.setGeneration_code(generationEntity);

            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setId(classInfoDTO.getId());
            classInfoEntity.setId(memberEntity);

            // 업데이트 된 데이터 저장
            classInfoRepository.save(classInfoEntity);

            return ClassInfoDTO.toClassInfoDTO(classInfoEntity);
        } else {
            throw new RuntimeException("ClassInfoEntity with class_num " + class_num + " not found");
        }
    }
    public void removeClassInfoByMemberId(int memberId) {
        classInfoRepository.removeClassInfoByMemberId(memberId);
    }
}
