package com.ASAF.service;

import com.ASAF.dto.LockerDTO;
import com.ASAF.entity.*;
import com.ASAF.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LockerService {

    private final LockerRepository lockerRepository;
    @Autowired
    private ClassInfoRepository classInfoRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private GenerationRepository generationRepository;
    @Autowired
    private MemberRepository memberRepository;

    // 배치 완료
    @Transactional
    public void completeLockers(List<LockerDTO> lockerDTOList) {
        List<LockerEntity> lockerEntities = new ArrayList<>();
        int classCode = lockerDTOList.get(0).getClass_code();
        int regionCode = lockerDTOList.get(0).getRegion_code();
        int generationCode = lockerDTOList.get(0).getGeneration_code();

        ClassEntity classEntity = classRepository.findById(classCode).orElseThrow(() -> new RuntimeException("ClassEntity not found for the given classCode"));
        RegionEntity regionEntity = regionRepository.findById(regionCode).orElseThrow(() -> new RuntimeException("RegionEntity not found for the given regionCode"));
        GenerationEntity generationEntity = generationRepository.findById(generationCode).orElseThrow(() -> new RuntimeException("GenerationEntity not found for the given generationCode"));

        lockerRepository.deleteByClassCodeAndRegionCodeAndGenerationCode(classEntity, regionEntity, generationEntity);

        Long maxLockerId = lockerRepository.findMaxLockerId();

        System.out.println("maxLockerId found: " + maxLockerId);
        // locker_id가 없는 경우 0L으로 설정합니다.
        if (maxLockerId == null) {
            maxLockerId = 0L;
            System.out.println("maxLockerId is set to " + maxLockerId);
        }

        for (LockerDTO lockerDTO : lockerDTOList) {
            LockerEntity lockerEntity = new LockerEntity();
            System.out.println(maxLockerId);
            maxLockerId++; // locker_id를 결정하기 위해 최댓값에서 1씩 증가시킵니다.
            lockerEntity.setLocker_id(maxLockerId); // 증가된 locker_id를 lockerEntity에 설정합니다.
            lockerEntity.setLocker_num(lockerDTO.getLocker_num());
            lockerEntity.setName(lockerDTO.getName());
            ClassInfoEntity classInfoEntity = classInfoRepository.findById(lockerDTO.getClass_num()).orElse(null);
            ClassEntity classEntity1 = classRepository.findById(lockerDTO.getClass_code()).orElse(null);
            RegionEntity regionEntity1 = regionRepository.findById(lockerDTO.getRegion_code()).orElse(null);
            GenerationEntity generationEntity1 = generationRepository.findById(lockerDTO.getGeneration_code()).orElse(null);
            MemberEntity memberEntity = memberRepository.findById(lockerDTO.getId()).orElse(null);

            lockerEntity.setClass_num(classInfoEntity);
            lockerEntity.setClass_code(classEntity1);
            lockerEntity.setRegion_code(regionEntity1);
            lockerEntity.setGeneration_code(generationEntity1);
            lockerEntity.setId(memberEntity);

            // 새로운 데이터를 추가
            lockerEntities.add(lockerEntity);
        }
        lockerRepository.saveAll(lockerEntities);
    }

    // 사물함 정보
    public List<LockerDTO> getAllLockers() {
        List<LockerEntity> lockerEntities = lockerRepository.findAll();
        return lockerEntities.stream()
                .map(lockerEntity -> new LockerDTO(lockerEntity))
                .collect(Collectors.toList());
    }

    // 개인 사물함 정보
    public LockerDTO getLockerByUser(int class_code, int region_code, int generation_code, int id) {
        Optional<LockerEntity> lockerEntityOptional = lockerRepository.findByClassCodeAndRegionCodeANdGenerationCodeAndId(class_code, region_code, generation_code, id);

        if (lockerEntityOptional.isPresent()) {
            LockerEntity lockerEntity = lockerEntityOptional.get();
            return new LockerDTO(lockerEntity);
        } else {
            throw new RuntimeException("Locker" + id + "not found");
        }
    }

    // 각 반의 사물함 정보
    public List<LockerDTO> getLockersByCodes(int class_code, int region_code, int generation_code) {
        List<LockerEntity> lockerEntities = lockerRepository.findByClassCodeAndRegionCodeAndGenerationCode(class_code, region_code, generation_code);
        return lockerEntities.stream()
                .map(lockerEntity -> new LockerDTO(lockerEntity))
                .collect(Collectors.toList());
    }
}
