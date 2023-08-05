package com.ASAF.config;

import com.ASAF.dto.*;
import com.ASAF.entity.*;
import com.ASAF.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final GenerationRepository generationRepository;
    private final ClassRepository classRepository;
    private final BusRepository busRepository;
    private final BookRepository bookRepository;
    private final ClassInfoRepository classInfoRepository;



    public DataLoader(MemberRepository memberRepository, RegionRepository regionRepository, GenerationRepository generationRepository, ClassRepository classRepository, BusRepository busRepository, BookRepository bookRepository, ClassInfoRepository classInfoRepository) {
        this.memberRepository = memberRepository;
        this.regionRepository = regionRepository;
        this.generationRepository = generationRepository;
        this.classRepository = classRepository;
        this.busRepository = busRepository;
        this.bookRepository = bookRepository;
        this.classInfoRepository = classInfoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String[] regionNames = {"서울", "구미", "대전", "부울경", "광주"};
        String[] generationNames = {"9기", "10기"};
        String[] bus_route = {"구미역 (파리바게트 앞)", "오성예식장 앞 (버스 정류장)", "구미상공회의소 건너 승강장", "형곡동 파리바게트 앞", "사곡 보성1차 (쪽쪽갈비 앞)", "우방신세계2차 (상모우방2단지 정류장)", "코오롱하늘채(정류장 지나 건널목)"};

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
            memberDTO.setProfile_image("src/main/resources/static/images/profile_images/ssafy" + i + "@email.com.png");
            memberDTO.setElectronic_student_id(10000 + i);
            memberDTO.setMember_info("D10" + i);
            memberDTO.setToken("fE_q5sa_RNKy7QkzhDar42:APA91bGHed0OzNm8ETlcMbzCFVNyXxs1moHW641-CQEN7PebBUjRKboMR8zg_HQfJuZiGFzShUDGD40zMWApLgZeBTFJckPfwN5za_LGm1txmE4EVcj8XqNDH81Vny__FwOwrLLM58Rh");
            memberDTO.setAttended("미출석");
            if (i == 1) {
                memberDTO.setAuthority("프로");
            } else {
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
        RegionEntity gumiRegionEntity = null;
        for (int i = 1; i <= regionNames.length; i++) {
            // 더미 데이터 생성을 위한 RegionDTO 객체 설정
            RegionDTO regionDTO = new RegionDTO();
            regionDTO.setRegion_code(i);
            regionDTO.setRegion_name(regionNames[i - 1]);

            // RegionDTO를 RegionEntity로 변환해서 저장
            RegionEntity regionEntity = RegionEntity.toRegionEntity(regionDTO);
            regionRepository.save(regionEntity);

            // 구미 지역 강조
            if (regionNames[i - 1].equals("구미")) {
                gumiRegionEntity = regionEntity;
            }
        }

        // 기수 더미데이터
        for (int i = 1; i <= generationNames.length; i++) {
            GenerationDTO generationDTO = new GenerationDTO();
            generationDTO.setGeneration_code(i);
            generationDTO.setGeneration_num(generationNames[i - 1]);
            GenerationEntity generationEntity = GenerationEntity.toGenerationEntity(generationDTO);
            generationRepository.save(generationEntity);
        }

        // 버스 더미데이터
        for (int i = 1; i <= bus_route.length; i++) {
            BusDTO busDTO = new BusDTO();
            busDTO.setBusNum(i);
            busDTO.setBus_route(bus_route[i - 1]);
            busDTO.setLocation("미출발");
            busDTO.setRegion_name("구미");
            BusEntity busEntity = BusEntity.toBusEntity(busDTO);
            busRepository.save(busEntity);
        }

        // 학급 더미데이터
        for (int i = 1; i <= 10; i++) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            if (i <= 2) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(1);
            } else {
                if (i % 2 == 0) {
                    classInfoDTO.setClass_code(1);
                    classInfoDTO.setId(i-1);
                } else {
                    classInfoDTO.setClass_code(2);
                    classInfoDTO.setId(i-1);
                }
            }
            classInfoDTO.setGeneration_code(1);
            classInfoDTO.setRegion_code(2);
            ClassInfoEntity classInfoEntity = ClassInfoEntity.toClassInfoEntity(classInfoDTO);
            classInfoRepository.save(classInfoEntity);
        }
        
        // 도서 더미데이터
        for (int i = 1; i <= 10; i++) {
            BookDTO bookDTO = new BookDTO();
            if (i==1){
                bookDTO.setBookName("책" + 1);
                bookDTO.setAuthor("저자 " + 1);
                bookDTO.setPublisher("출판사" + 1);
                bookDTO.setBorrowState(true);
                bookDTO.setId(2);
                bookDTO.setClass_code(2);
                bookDTO.setRegion_code(1);
                bookDTO.setGeneration_code(1);
            }else if(i<=3){
                bookDTO.setBookName("책" + 1);
                bookDTO.setAuthor("저자 " + 1);
                bookDTO.setPublisher("출판사" + 1);
                bookDTO.setBorrowState(false);
                bookDTO.setId(1);
                bookDTO.setClass_code(1);
                bookDTO.setRegion_code(2);
                bookDTO.setGeneration_code(1);
            }else{
                bookDTO.setBookName("책" + (i-3));
                bookDTO.setAuthor("저자 " + (i-3));
                bookDTO.setPublisher("출판사" + (i-3));
                bookDTO.setBorrowState(false);
                bookDTO.setId(1);
                bookDTO.setClass_code(1);
                bookDTO.setRegion_code(2);
                bookDTO.setGeneration_code(1);
            }
            BookEntity bookEntity = BookEntity.toBookEntity(bookDTO);
            bookRepository.save(bookEntity);
        }

    }
}
