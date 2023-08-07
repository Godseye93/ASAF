package com.ASAF.config;

import com.ASAF.dto.*;
import com.ASAF.entity.*;
import com.ASAF.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Random;
import java.util.stream.Collectors;


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


        Random random = new Random();
        // 멤버 더미데이터
        for (int i = 1; i <= 1024; i++) {
            // 더미 데이터 생성을 위한 MemberDTO 객체 설정
            MemberDTO memberDTO = new MemberDTO();

            String randomLetters = random.ints(6, 'a', 'z' + 1) // 범위는 a에서 z 사이
                    .mapToObj(n -> (char) n)
                    .map(Object::toString)
                    .collect(Collectors.joining());

            int randomNumber = random.nextInt(10000); // 범위는 0에서 9999 사이

            String randomEmail = String.format("%s%04d@ssafy.com", randomLetters, randomNumber);
            memberDTO.setMemberEmail(randomEmail);

            String randomPassword = String.format("%04d", random.nextInt(10000));
            memberDTO.setMemberPassword(randomPassword);

            String randomName = "User" + String.format("%04d", random.nextInt(10000));
            memberDTO.setMemberName(randomName);

            if (i <= 24) {
                memberDTO.setStudent_number("00000");
            } else {
                String studentNumber = String.format("%05d", i - 24);
                memberDTO.setStudent_number(studentNumber);
            }

            int birthYear = 1993 + random.nextInt(10);  // 생년 범위: 1960 ~ 1999
            String birthDate = String.format("%d-%02d-%02d", birthYear, 1 + random.nextInt(12), 1 + random.nextInt(28));
            memberDTO.setBirth_date(birthDate);

            String phoneNumber = String.format("010-%04d-%04d", 1000 + random.nextInt(9000), 1000 + random.nextInt(9000));
            memberDTO.setPhone_number(phoneNumber);

            memberDTO.setProfile_image("src/main/resources/static/images/profile_images/ssafy" + 1 + "@email.com.png");
            memberDTO.setElectronic_student_id(10000 + i);

            if (i <= 544) {
                memberDTO.setMember_info("0000");
            } else {
                int randomInfoNumber = 101 + random.nextInt(10); // 범위는 101에서 110
                String memberInfo = "D" + randomInfoNumber;
                memberDTO.setMember_info(memberInfo);
            }
            memberDTO.setToken("fE_q5sa_RNKy7QkzhDar42:APA91bGHed0OzNm8ETlcMbzCFVNyXxs1moHW641-CQEN7PebBUjRKboMR8zg_HQfJuZiGFzShUDGD40zMWApLgZeBTFJckPfwN5za_LGm1txmE4EVcj8XqNDH81Vny__FwOwrLLM58Rh");
            memberDTO.setAttended("미출석");
            if (i <= 24) {
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
        for (int i = 1; i <= 1038; i++) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            if (i <= 2) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(1);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 4) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(2);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 6) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(3);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 8) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(4);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 10) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(5);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 12) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(6);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 14) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(7);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 16) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(8);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 18) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(9);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 20) {
                classInfoDTO.setClass_code(i);
                classInfoDTO.setId(10);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 30) {
                classInfoDTO.setClass_code(i-20);
                classInfoDTO.setId(i-10);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 33) {
                classInfoDTO.setClass_code(i - 30);
                classInfoDTO.setId(21);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(2);
            } else if (i <= 36) {
                classInfoDTO.setClass_code(i - 30);
                classInfoDTO.setId(22);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(2);
            } else if (i == 37) {
                classInfoDTO.setClass_code(1);
                classInfoDTO.setId(23);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(2);
            } else if (i == 38) {
                classInfoDTO.setClass_code(2);
                classInfoDTO.setId(24);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(2);
            } else if (i <= 58) {
                classInfoDTO.setClass_code(1);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 78) {
                classInfoDTO.setClass_code(2);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 98) {
                classInfoDTO.setClass_code(3);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 118) {
                classInfoDTO.setClass_code(4);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 138) {
                classInfoDTO.setClass_code(5);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 158) {
                classInfoDTO.setClass_code(6);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 178) {
                classInfoDTO.setClass_code(7);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 198) {
                classInfoDTO.setClass_code(8);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 218) {
                classInfoDTO.setClass_code(9);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i <= 238) {
                classInfoDTO.setClass_code(10);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(1);
            } else if (i<=278) {
                classInfoDTO.setClass_code(1);
                classInfoDTO.setId(i-14);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(1);
            } else if (i<=318) {
                classInfoDTO.setClass_code(2);
                classInfoDTO.setId(i - 14);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(1);
            } else if (i<=338) {
                classInfoDTO.setClass_code(1);
                classInfoDTO.setId(i - 14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(2);
            } else if (i <= 358) {
                classInfoDTO.setClass_code(2);
                classInfoDTO.setId(i - 14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(2);
            } else if (i <= 378) {
                classInfoDTO.setClass_code(3);
                classInfoDTO.setId(i - 14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(2);
            } else if (i <= 398) {
                classInfoDTO.setClass_code(4);
                classInfoDTO.setId(i - 14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(2);
            } else if (i <= 418) {
                classInfoDTO.setClass_code(5);
                classInfoDTO.setId(i - 14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(2);
            } else if (i <= 438) {
                classInfoDTO.setClass_code(6);
                classInfoDTO.setId(i - 14);
                classInfoDTO.setGeneration_code(2);
                classInfoDTO.setRegion_code(2);
            } else if (i<=478) {
                classInfoDTO.setClass_code(1);
                classInfoDTO.setId(i - 14);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(2);
            } else if (i<=518) {
                classInfoDTO.setClass_code(1);
                classInfoDTO.setId(i - 14);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(2);
            } else {
                classInfoDTO.setClass_code(1);
                classInfoDTO.setId(i - 14);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(2);
            }

            ClassInfoEntity classInfoEntity = ClassInfoEntity.toClassInfoEntity(classInfoDTO);
            classInfoRepository.save(classInfoEntity);
        }
        
        // 도서 더미데이터
        for (int i = 1; i <= 12; i++) {
            BookDTO bookDTO = new BookDTO();
            if (i<=10) {
                bookDTO.setBorrowState(false);
                bookDTO.setId(1);
                bookDTO.setClass_code(i);
                bookDTO.setRegion_code(1);
                bookDTO.setGeneration_code(1);
                for (int j=1; j<=10; j++) {
                    bookDTO.setBookName("책" + 1);
                    bookDTO.setAuthor("저자 " + 1);
                    bookDTO.setPublisher("출판사" + 1);
                }
            } else if (i==11){
                bookDTO.setBorrowState(false);
                bookDTO.setId(1);
                bookDTO.setClass_code(1);
                bookDTO.setRegion_code(2);
                bookDTO.setGeneration_code(1);
            } else if (i==12) {
                bookDTO.setBorrowState(false);
                bookDTO.setId(1);
                bookDTO.setClass_code(2);
                bookDTO.setRegion_code(2);
                bookDTO.setGeneration_code(1);
            }


//            if (i==1){
//                bookDTO.setBookName("책" + 1);
//                bookDTO.setAuthor("저자 " + 1);
//                bookDTO.setPublisher("출판사" + 1);
//                bookDTO.setBorrowState(true);
//                bookDTO.setId(2);
//                bookDTO.setClass_code(2);
//                bookDTO.setRegion_code(1);
//                bookDTO.setGeneration_code(1);
//            }else if(i<=3){
//                bookDTO.setBookName("책" + 1);
//                bookDTO.setAuthor("저자 " + 1);
//                bookDTO.setPublisher("출판사" + 1);
//                bookDTO.setBorrowState(false);
//                bookDTO.setId(1);
//                bookDTO.setClass_code(1);
//                bookDTO.setRegion_code(2);
//                bookDTO.setGeneration_code(1);
//            }else{
//                bookDTO.setBookName("책" + (i-3));
//                bookDTO.setAuthor("저자 " + (i-3));
//                bookDTO.setPublisher("출판사" + (i-3));
//                bookDTO.setBorrowState(false);
//                bookDTO.setId(1);
//                bookDTO.setClass_code(1);
//                bookDTO.setRegion_code(2);
//                bookDTO.setGeneration_code(1);
//            }
            BookEntity bookEntity = BookEntity.toBookEntity(bookDTO);
            bookRepository.save(bookEntity);
        }

    }
}
