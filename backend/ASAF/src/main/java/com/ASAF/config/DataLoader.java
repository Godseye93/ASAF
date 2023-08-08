/* 
master branch로 push 시 수정해야 할 것
1. 더미데이터 주석 처리.
1-2. 더미데이터 사용할 땐 이미지 경로 변경
2. memberService 이미지 save하는 메서드 경로 변경
3. property create에서 update로 변경
 */


package com.ASAF.config;

import com.ASAF.dto.*;
import com.ASAF.entity.*;
import com.ASAF.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
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

            if (i<=24) {
                String email = String.format("ssafypro%04d@ssafy.com", i);
                memberDTO.setMemberEmail(email);
            } else if (i >= 439 && i <= 449) {
                int suffix = i - 438; // 범위가 1부터 시작하도록 하기 위해 438를 빼줍니다.
                String email = String.format("ssafy%04d@ssafy.com", suffix);
                memberDTO.setMemberEmail(email);
            } else {
                String randomEmail = String.format("%s%04d@ssafy.com", randomLetters, randomNumber);
                memberDTO.setMemberEmail(randomEmail);
            }

            if (i<=24) {
                memberDTO.setMemberPassword("0000");
            } else if (i >= 439 && i <= 449) {
                memberDTO.setMemberPassword("0000");
            } else {
                String randomPassword = String.format("%04d", random.nextInt(10000));
                memberDTO.setMemberPassword(randomPassword);
            }

            List<String> exampleNames = Arrays.asList(
                    "김태희", "박시환", "정지민", "이성민", "조영훈", "장지수",
                    "송해솔", "유지안", "주아린", "박세준", "양주원", "최예린",
                    "황소희", "서시은", "임하윤", "이한나", "강주영", "한승훈",
                    "조민서", "신수도", "유희진", "남지원", "문혜림", "임예원",
                    "진서희", "최윤표", "류은주", "이승호", "김하영", "박동원",
                    "강지숙", "남승연", "서연우", "최인준", "임민준", "유경민",
                    "박서은", "양예지", "조원서", "문도현", "김준희", "이가령",
                    "송미수", "진윤창", "장태호", "허민진", "홍승환", "이우승",
                    "성민기", "유민서", "박재원", "김상준", "서주리", "오재후",
                    "류선영", "조영규", "노경아", "정윤재", "한나리", "권수현",
                    "전수진", "신예진", "나연서", "김성우", "이자인", "박희주",
                    "송지원", "류하림", "안서연", "조성윤", "원승주", "장지윤",
                    "김유진", "황수민", "이우진", "정하은", "조민지", "류재영",
                    "김주리", "조혜준", "배육희", "최은영", "한유경", "김윤선",
                    "허다영", "이나리", "서민수", "표선미", "장혜진", "조우찬",
                    "강민경", "황진우", "김상영", "오지연", "임성훈", "김채원",
                    "윤소희", "박상윤", "이승연", "김현우", "전지민", "서영수",
                    "남예원", "한동영", "황수영", "신은경", "천승민", "진재윤",
                    "김유리", "노가영", "가은경", "장호연", "옥용재", "김영준",
                    "김태경", "하지은", "최지원", "강민규", "석하림", "조미진"
            );

            int randomIndex = random.nextInt(exampleNames.size());
            String randomName = exampleNames.get(randomIndex);
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


//            memberDTO.setProfile_image("src/main/resources/static/images/profile_images/default.png");
            memberDTO.setProfile_image("/home/ubuntu/statics/images/profile_images/default.png");
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

        List<List<String>> bookAuthorList = Arrays.asList(
                Arrays.asList("김영한과 함께하는 즐거운 스프링부트", "김영한"),
                Arrays.asList("김영한과 함께하는 지옥의 스프링부트", "김영한"),
                Arrays.asList("Vue.js 철저 입문", "박재구"),
                Arrays.asList("예제로 배우는 블록체인", "한영환"),
                Arrays.asList("머신 러닝 교과서", "한문철"),
                Arrays.asList("리엑트 디자인 패턴과 모범 사례", "이은지"),
                Arrays.asList("Docker", "안드류"),
                Arrays.asList("AWS 인프라 구축 가이드", "김수민"),
                Arrays.asList("모던 자바 스크립트", "권종민"),
                Arrays.asList("CISCO 네트워킹", "박나래")
        );

        for (int i = 1; i <= 12; i++) {
            if (i <= 10) {
                for (int j = 1; j <= 30; j++) {
                    BookDTO bookDTO = new BookDTO();
                    List<String> bookAuthor = bookAuthorList.get((j - 1) % 10);
                    String book = bookAuthor.get(0);
                    String author = bookAuthor.get(1);

                    bookDTO.setBorrowState(false);
                    bookDTO.setId(1);
                    bookDTO.setClass_code(i);
                    bookDTO.setRegion_code(1);
                    bookDTO.setGeneration_code(1);
                    bookDTO.setBookName(book);
                    bookDTO.setAuthor(author);
                    bookDTO.setPublisher("싸피출판사");

                    BookEntity bookEntity = BookEntity.toBookEntity(bookDTO);
                    bookRepository.save(bookEntity);
                }
            } else if (i == 11) {
                for (int j = 1; j <= 30; j++) {
                    BookDTO bookDTO = new BookDTO();
                    List<String> bookAuthor = bookAuthorList.get((j - 1) % 10);
                    String book = bookAuthor.get(0);
                    String author = bookAuthor.get(1);

                    bookDTO.setBorrowState(false);
                    bookDTO.setId(1);
                    bookDTO.setClass_code(1);
                    bookDTO.setRegion_code(2);
                    bookDTO.setGeneration_code(1);
                    bookDTO.setBookName(book);
                    bookDTO.setAuthor(author);
                    bookDTO.setPublisher("싸피출판사");

                    BookEntity bookEntity = BookEntity.toBookEntity(bookDTO);
                    bookRepository.save(bookEntity);
                }
            } else if (i == 12) {
                for (int j = 1; j <= 30; j++) {
                    BookDTO bookDTO = new BookDTO();
                    List<String> bookAuthor = bookAuthorList.get((j - 1) % 10);
                    String book = bookAuthor.get(0);
                    String author = bookAuthor.get(1);

                    bookDTO.setBorrowState(false);
                    bookDTO.setId(1);
                    bookDTO.setClass_code(2);
                    bookDTO.setRegion_code(2);
                    bookDTO.setGeneration_code(1);
                    bookDTO.setBookName(book);
                    bookDTO.setAuthor(author);
                    bookDTO.setPublisher("싸피출판사");

                    BookEntity bookEntity = BookEntity.toBookEntity(bookDTO);
                    bookRepository.save(bookEntity);
                }
            }
        }
    }
}
