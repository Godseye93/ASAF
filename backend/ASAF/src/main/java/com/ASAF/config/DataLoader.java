package com.ASAF.config;

import com.ASAF.dto.ClassDTO;
import com.ASAF.dto.GenerationDTO;
import com.ASAF.dto.MemberDTO;
import com.ASAF.dto.RegionDTO;
import com.ASAF.entity.ClassEntity;
import com.ASAF.entity.GenerationEntity;
import com.ASAF.entity.MemberEntity;
import com.ASAF.entity.RegionEntity;
import com.ASAF.repository.ClassRepository;
import com.ASAF.repository.GenerationRepository;
import com.ASAF.repository.MemberRepository;
import com.ASAF.repository.RegionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final GenerationRepository generationRepository;
    private final ClassRepository classRepository;


    public DataLoader(MemberRepository memberRepository, RegionRepository regionRepository, GenerationRepository generationRepository, ClassRepository classRepository) {
        this.memberRepository = memberRepository;
        this.regionRepository = regionRepository;
        this.generationRepository = generationRepository;
        this.classRepository = classRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String[] regionNames = {"서울", "구미", "대전", "부울경", "광주"};
        String[] generationNames = {"9기","10기"};
        
        // 멤버 더미데이터
        for (int i = 1; i <= 9; i++) {
            // 더미 데이터 생성을 위한 MemberDTO 객체 설정
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMemberEmail("ssafy" + i + "@email.com");
            memberDTO.setMemberPassword("q1w2e3r" + i);
            memberDTO.setMemberName("싸피인" + i);
            memberDTO.setStudent_number("11111" + i);
            memberDTO.setBirth_date("199" + i + "-01-01");
            memberDTO.setPhone_number("010-1234-000" + i);
            memberDTO.setProfile_image("image" + i + ".jpg");
            memberDTO.setElectronic_student_id(10000 + i);
            memberDTO.setMember_info("D10" + i);
            if (i == 1){
                memberDTO.setAuthority("프로");
            }else{
                memberDTO.setAuthority("교육생");
            }

            // MemberDTO를 MemberEntity로 변환해서 저장
            MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
            memberRepository.save(memberEntity);
        }
        // 반 더미데이터
        for (int i = 1; i <= 20; i++) {
            ClassDTO classDTO = new ClassDTO();
            classDTO.setClassname(i + "반");
            ClassEntity classEntity = ClassEntity.toClassEntity(classDTO);
            classRepository.save(classEntity);
        }
        
        // 지역 더미데이터
        for (int i = 1; i <= regionNames.length; i++) {
            // 더미 데이터 생성을 위한 RegionDTO 객체 설정
            RegionDTO regionDTO = new RegionDTO();
            regionDTO.setRegion_code(i);
            regionDTO.setRegion_name(regionNames[i - 1]);

            // RegionDTO를 RegionEntity로 변환해서 저장
            RegionEntity regionEntity = RegionEntity.toRegionEntity(regionDTO);
            regionRepository.save(regionEntity);
        }
        
        // 기수 더미데이터
        for (int i = 1; i <= generationNames.length; i++) {
            GenerationDTO generationDTO = new GenerationDTO();
            generationDTO.setGeneration_code(i);
            generationDTO.setGeneration_num(generationNames[i - 1]);
            GenerationEntity generationEntity = GenerationEntity.toGenerationEntity(generationDTO);
            generationRepository.save(generationEntity);
        }
    }
}
