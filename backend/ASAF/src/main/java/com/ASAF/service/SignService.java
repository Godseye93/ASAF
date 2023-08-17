package com.ASAF.service;

import com.ASAF.dto.SignDTO;
import com.ASAF.entity.*;
import com.ASAF.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SignService {

    private final SignRepository signRepository;
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

    // post 정보 받아서 DB에 저장하기
    public void saveImageUrl(SignDTO signDTO, MultipartFile file) throws IOException {
        MemberEntity memberEntity = memberRepository.findById(signDTO.getId()).orElseThrow(() -> new RuntimeException("MemberEntity not found for the given userId"));

        // 이미지 저장 및 엔티티 업데이트 등의 작업을 수행합니다
        String UPLOAD_DIR = "/home/ubuntu/statics/images/sign_images/";
//        String UPLOAD_DIR = "src/main/resources/static/images/sign_images/";
        String real_DIR = "images/sign_images/";
        String fileName = signDTO.getMonth() + "_" + file.getOriginalFilename();
        String filePath = UPLOAD_DIR + signDTO.getName() + "_" + fileName;
        String realPath = real_DIR + signDTO.getName() + "_" + fileName;
        File dest = new File(filePath);
        FileCopyUtils.copy(file.getBytes(), dest);


        ClassInfoEntity classInfoEntity = classInfoRepository.findById(signDTO.getClass_num()).orElse(null);
        ClassEntity classEntity1 = classRepository.findById(signDTO.getClass_code()).orElse(null);
        RegionEntity regionEntity1 = regionRepository.findById(signDTO.getRegion_code()).orElse(null);
        GenerationEntity generationEntity1 = generationRepository.findById(signDTO.getGeneration_code()).orElse(null);

        List<SignEntity> existingSigns = signRepository.findByNameAndMonth(signDTO.getName(), signDTO.getMonth());
        if (!existingSigns.isEmpty()) {
            signRepository.deleteAll(existingSigns);
        }

        SignEntity signEntity = new SignEntity();
//        signEntity.setImage_url(filePath);
        signEntity.setImage_url(realPath);
        signEntity.setName(signDTO.getName());
        signEntity.setMonth(signDTO.getMonth());
        signEntity.setClass_num(classInfoEntity);
        signEntity.setClass_code(classEntity1);
        signEntity.setRegion_code(regionEntity1);
        signEntity.setGeneration_code(generationEntity1);
        signEntity.setId(memberEntity);

        signRepository.save(signEntity);
    }

    // get 으로 클라이언트한테 보내기
    public String getImageUrlPath(String name) throws ChangeSetPersister.NotFoundException {
        SignEntity signEntity = signRepository.findByName(name)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        return signEntity.getImage_url();
    }

    // 서명서 이미지, 이름 반환
    public List<SignDTO> getSignsByCodes(int class_code, int region_code, int generation_code, String month) {
        List<SignEntity> signEntities = signRepository.findByClassEntityClassCodeAndRegionEntityRegionCodeAndGenerationEntityGenerationCodeAndMonth(class_code, region_code, generation_code, month);
        return signEntities.stream()
                .map(signEntity -> new SignDTO(signEntity))
                .collect(Collectors.toList());
    }
}
