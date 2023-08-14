package com.ASAF.service;

import com.ASAF.dto.SeatDTO;
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
public class SeatService {

    private final SeatRepository seatRepository;
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
    public void completeSeats(List<SeatDTO> seatDTOList) {
        List<SeatEntity> seatEntities = new ArrayList<>();

        int classCode = seatDTOList.get(0).getClass_code();
        int regionCode = seatDTOList.get(0).getRegion_code();
        int generationCode = seatDTOList.get(0).getGeneration_code();

        ClassEntity classEntity = classRepository.findById(classCode).orElseThrow(() -> new RuntimeException("ClassEntity not found for the given classCode"));
        RegionEntity regionEntity = regionRepository.findById(regionCode).orElseThrow(() -> new RuntimeException("RegionEntity not found for the given regionCode"));
        GenerationEntity generationEntity = generationRepository.findById(generationCode).orElseThrow(() -> new RuntimeException("GenerationEntity not found for the given generationCode"));

        seatRepository.deleteByClassCodeAndRegionCodeAndGenerationCode(classEntity, regionEntity, generationEntity);

        for (SeatDTO seatDTO : seatDTOList) {
            SeatEntity seatEntity = new SeatEntity();
            seatEntity.setSeat_id(seatDTO.getSeat_id());
            seatEntity.setSeat_num(seatDTO.getSeat_num());
            seatEntity.setName(seatDTO.getName());
            ClassInfoEntity classInfoEntity = classInfoRepository.findById(seatDTO.getClass_num()).orElse(null);
            ClassEntity classEntity1 = classRepository.findById(seatDTO.getClass_code()).orElse(null);
            RegionEntity regionEntity1 = regionRepository.findById(seatDTO.getRegion_code()).orElse(null);
            GenerationEntity generationEntity1 = generationRepository.findById(seatDTO.getGeneration_code()).orElse(null);
            MemberEntity memberEntity = memberRepository.findById(seatDTO.getId()).orElse(null);

            seatEntity.setClass_num(classInfoEntity);
            seatEntity.setClass_code(classEntity1);
            seatEntity.setRegion_code(regionEntity1);
            seatEntity.setGeneration_code(generationEntity1);
            seatEntity.setId(memberEntity);

            // 새로운 데이터를 추가
            seatEntities.add(seatEntity);
        }
        seatRepository.saveAll(seatEntities);
    }

    // 자리 정보
    public List<SeatDTO> getAllSeats() {
        List<SeatEntity> seatEntities = seatRepository.findAll();
        return seatEntities.stream()
                .map(seatEntity -> new SeatDTO(seatEntity))
                .collect(Collectors.toList());
    }

    // 개인 자리 정보
    public SeatDTO getSeatByUser(int class_code, int region_code, int generation_code, int id) {
        Optional<SeatEntity> seatEntityOptional = seatRepository.findByClassCodeAndRegionCodeAndGenerationCodeAndId(class_code, region_code, generation_code, id);

        if (seatEntityOptional.isPresent()) {
            SeatEntity seatEntity = seatEntityOptional.get();
            return new SeatDTO(seatEntity);
        } else {
            throw new RuntimeException("Seat for student" + id + "not found");
        }
    }

    public List<SeatDTO> getSeatsByCodes(int class_code, int region_code, int generation_code) {
        List<SeatEntity> seatEntities = seatRepository.findByClassCodeAndRegionCodeAndGenerationCode(class_code, region_code, generation_code);
        return seatEntities.stream()
                .map(seatEntity -> new SeatDTO(seatEntity))
                .collect(Collectors.toList());
    }
}
