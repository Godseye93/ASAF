/*
master branch로 push 시 수정해야 할 것
1. 더미데이터 주석 처리.
1-2. 더미데이터 사용할 땐 이미지 경로 변경
2. memberService 이미지 save하는 메서드 경로 변경
3. property create에서 update로 변경
4. FirebaseCloudMessagingInitializer에서 String jsonPath = "/home/ubuntu/ASAF_FCM_KEY.json"; 로 변경
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
    private final SeatRepository seatRepository;
    private final LockerRepository lockerRepository;
    private final PostRepository postRepository;



    public DataLoader(MemberRepository memberRepository, RegionRepository regionRepository, GenerationRepository generationRepository, ClassRepository classRepository, BusRepository busRepository, BookRepository bookRepository, ClassInfoRepository classInfoRepository, SeatRepository seatRepository, LockerRepository lockerRepository, PostRepository postRepository) {
        this.memberRepository = memberRepository;
        this.regionRepository = regionRepository;
        this.generationRepository = generationRepository;
        this.classRepository = classRepository;
        this.busRepository = busRepository;
        this.bookRepository = bookRepository;
        this.classInfoRepository = classInfoRepository;
        this.seatRepository = seatRepository;
        this.lockerRepository = lockerRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String[] regionNames = {"서울", "구미", "대전", "부울경", "광주"};
        String[] generationNames = {"9기", "10기"};
        String[] bus_route = {"구미역 (파리바게트 앞)", "오성예식장 앞 (버스 정류장)", "구미상공회의소 건너 승강장", "형곡동 파리바게트 앞", "사곡 보성1차 (쪽쪽갈비 앞)", "우방신세계2차 (상모우방2단지 정류장)", "코오롱하늘채(정류장 지나 건널목)"};


        Random random = new Random();
        // 멤버 더미데이터
        for (int i = 1; i <= 102; i++) {
            // 더미 데이터 생성을 위한 MemberDTO 객체 설정
            MemberDTO memberDTO = new MemberDTO();

            List<String> gumipro = Arrays.asList("ssafypro0001@ssafy.com", "ssafypro0002@ssafy.com");
            List<String> gumi2Class = Arrays.asList(
                    "rlaxogml", "qkrtlghks", "wjdwlals", "dltjdals", "whdudgns", "wldwltn",
                    "thdgothf", "dbwldks", "wndkfls", "qkrtpwns", "didwndnjs", "chldPfls",
                    "ghkdthgml", "tjtldms", "dlagkdbs", "dlgkssk", "rkdwndud", "gkstmdgns",
                    "whalstj", "tlstneh"
            );

            List<String> gumi1Class = Arrays.asList(
                    "cktjsgh", "rlagmldnd", "dldudwns", "rlawpwns", "chldudxo", "chlgPdnjs",
                    "rladmsgk", "qkrtjddnr", "dhgmlwn", "dlgustjq", "wkdtjddns", "chlgmlwn",
                    "rlawnstjd", "chlwodnjs", "qkrgusdn", "wkdwlsdnr", "rnjsalswo", "rlagudwls",
                    "rlaehdus", "rlawlsdud", "dlgusrms", "ghkdgkdma", "wjddpdnjs", "thswnsqo",
                    "rlawndyd", "ghkdtlsdns", "qkrwnsdn", "wjdwlsdnr", "wjstnfla", "rlatnals",
                    "dldncjf", "dbdudtj", "tjehdgus", "rlatjsdud", "qkrwhddnr",
                    "tjdmswls", "dltjgusm", "qkdwlstjd", "dlduswl", "wjddPwls",
                    "wjdtjswo", "rnjsalsdn", "tladmswls", "tjwlgh", "rlatncks", "rkdtmdgus",
                    "rlaalstn", "rladbwjd", "wlsgmltha", "qkreksql", "qkralsdk", "wjdwodnr",
                    "rlawjdghks", "rlarmsdn", "cjsqudcks", "corudgh", "rlatkdwls", "rlatmfrl",
                    "tlswlgns", "tjddustjr", "thdalscjf", "tlswpgud", "dlwjdgns", "gktnqls",
                    "skadPdnjs", "gksehddud", "ghkdtndud", "tlsdmsrud", "cjstmdals", "wlswodbs",
                    "rladbfkl", "shrkdud", "rkdmsrud", "wkdghdus", "dhrdydwo", "rladudwns",
                    "rlaxorud", "gkwldms", "chlwldnjs", "rkdalsrb"

            );


            if (i<=2) {
                String email = gumipro.get(i-1);
                memberDTO.setMemberEmail(email);
            } else if (i <= 82) {
                int number = i - 2;
                String email = gumi1Class.get(number-1) + "@ssafy.com";
                memberDTO.setMemberEmail(email);
            } else {
                int number = i - 82;
                String email = gumi2Class.get(number - 1) + "@ssafy.com";
                memberDTO.setMemberEmail(email);
            }
            memberDTO.setMemberPassword("0000");

            List<String> gumiproName = Arrays.asList("정수빈", "박민하");

            List<String> exampleNames = Arrays.asList(
                    "김태희", "박시환", "정지민", "이성민", "조영훈", "장지수",
                    "송해솔", "유지안", "주아린", "박세준", "양주원", "최예린",
                    "황소희", "서시은", "임하윤", "이한나", "강주영", "한승훈",
                    "조민서", "신수도"
            );

            List<String> realNames = Arrays.asList(
                    "차선호", "김희웅", "이영준", "김제준", "최영태", "최혜원",
                    "김은하", "박성욱", "오희주", "이현섭", "장성운", "최희주",
                    "김준성", "최재원", "박현우", "장진욱", "권민재", "김형진",
                    "김도연", "김진영", "이현근", "황하음", "정예원", "손준배",
                    "김주용", "황신운", "박준후", "정진욱", "전수림", "김수민",
                    "이우철", "유영서", "서동현", "김선영", "박종욱",
                    "서은진", "이서현", "방진성", "이연지", "정예진",
                    "정선재", "권민우", "심은진", "서지호", "김수찬",	"강승현",
                    "김민수", "김유정", "진희솜", "박단비", "박민아", "정재욱",
                    "김정환", "김근우", "천병찬", "채경호", "김상진", "김슬기",
                    "신지훈", "성연석", "송민철", "신제형", "이정훈", "하수빈",
                    "남예원", "한동영", "황수영", "신은경", "천승민", "진재윤",
                    "김유리", "노가영", "가은경", "장호연", "옥용재", "김영준",
                    "김태경", "하지은", "최지원", "강민규"
            );

            if (i<=2) {
                String name = gumiproName.get(i-1);
                memberDTO.setMemberName(name);
            } else if (i <= 82) {
                int number = i - 2;
                String name = realNames.get(number-1);
                memberDTO.setMemberName(name);
            } else {
                int number = i - 82;
                String name = exampleNames.get(number - 1);
                memberDTO.setMemberName(name);
            }


            if (i <= 2) {
                memberDTO.setStudent_number("00000");
            } else {
                String studentNumber = String.format("092" + "%03d", i - 2);
                memberDTO.setStudent_number(studentNumber);
            }

            int birthYear = 1993 + random.nextInt(10);  // 생년 범위: 1960 ~ 1999
            String birthDate = String.format("%d-%02d-%02d", birthYear, 1 + random.nextInt(12), 1 + random.nextInt(28));
            memberDTO.setBirth_date(birthDate);

            String phoneNumber = String.format("010-%04d-%04d", 1000 + random.nextInt(9000), 1000 + random.nextInt(9000));
            memberDTO.setPhone_number(phoneNumber);


            if (i <= 2) {
                String name = gumiproName.get(i-1);
                memberDTO.setProfile_image("/home/ubuntu/statics/images/profile_images/" + name + ".png");
            } else if (i <= 82) {
                String name = realNames.get(i-3);
                memberDTO.setProfile_image("/home/ubuntu/statics/images/profile_images/" + name + ".png");
            } else {
                String name = exampleNames.get(i-83);
                memberDTO.setProfile_image("/home/ubuntu/statics/images/profile_images/" + name + ".png");

            }
//            memberDTO.setProfile_image("src/main/resources/static/images/profile_images/default.png");


            memberDTO.setElectronic_student_id(10000 + i);
            if (i <= 2) {
                memberDTO.setMember_info("0000");
            } else if (i <= 8) {
                String memberInfo = "D101";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 14) {
                String memberInfo = "D102";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 20) {
                String memberInfo = "D103";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 26) {
                String memberInfo = "D104";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 32) {
                String memberInfo = "D105";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 37) {
                String memberInfo = "D106";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 42) {
                String memberInfo = "D107";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 48) {
                String memberInfo = "D108";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 54) {
                String memberInfo = "D109";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 60) {
                String memberInfo = "D110";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 66) {
                String memberInfo = "D111";
                memberDTO.setMember_info(memberInfo);

            } else if (i <= 72) {
                String memberInfo = "D112";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 78) {
                String memberInfo = "D113";
                memberDTO.setMember_info(memberInfo);
            } else if (i <= 82){
                String memberInfo = "D114";
                memberDTO.setMember_info(memberInfo);
            } else {
                memberDTO.setMember_info("0000");
            }

            memberDTO.setToken("fE_q5sa_RNKy7QkzhDar42:APA91bGHed0OzNm8ETlcMbzCFVNyXxs1moHW641-CQEN7PebBUjRKboMR8zg_HQfJuZiGFzShUDGD40zMWApLgZeBTFJckPfwN5za_LGm1txmE4EVcj8XqNDH81Vny__FwOwrLLM58Rh");
            memberDTO.setAttended("미출석");
            if (i <= 2) {
                memberDTO.setAuthority("프로");
            } else {
                memberDTO.setAuthority("교육생");
            }

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
        for (int i = 1; i <= 102; i++) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            if (i == 1) {
                classInfoDTO.setClass_code(1);
                classInfoDTO.setId(i);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(2);
            } else if (i == 2) {
                classInfoDTO.setClass_code(2);
                classInfoDTO.setId(i);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(2);
            } else if (i <= 82) {
                classInfoDTO.setClass_code(1);
                classInfoDTO.setId(i);
                classInfoDTO.setGeneration_code(1);
                classInfoDTO.setRegion_code(2);
            } else{
                classInfoDTO.setClass_code(2);
                classInfoDTO.setId(i);
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

        // 북 더미 생산
        for (int i = 1; i <= 2; i++) {
            if (i == 1) {
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
                    bookDTO.setClass_num(1);

                    BookEntity bookEntity = BookEntity.toBookEntity(bookDTO);
                    bookRepository.save(bookEntity);
                }
            } else {
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
                    bookDTO.setClass_num(1);

                    BookEntity bookEntity = BookEntity.toBookEntity(bookDTO);
                    bookRepository.save(bookEntity);
                }
            }
        }

        List<String> realNames = Arrays.asList(
                "차선호", "김희웅", "이영준", "김제준", "최영태", "최혜원",
                "김은하", "박성욱", "오희주", "이현섭", "장성운", "최희주",
                "김준성", "최재원", "박현우", "장진욱", "권민재", "김형진",
                "김도연", "김진영", "이현근", "황하음", "정예원", "손준배",
                "김주용", "황신운", "박준후", "정진욱", "전수림", "김수민",
                "이우철", "유영서", "서동현", "김선영", "박종욱",
                "서은진", "이서현", "방진성", "이연지", "정예진",
                "정선재", "권민우", "심은진", "서지호", "김수찬",	"강승현",
                "김민수", "김유정", "진희솜", "박단비", "박민아", "정재욱",
                "김정환", "김근우", "천병찬", "채경호", "김상진", "김슬기",
                "신지훈", "성연석", "송민철", "신제형", "이정훈", "하수빈",
                "남예원", "한동영", "황수영", "신은경", "천승민", "진재윤",
                "김유리", "노가영", "가은경", "장호연", "옥용재", "김영준",
                "김태경", "하지은", "최지원", "강민규"
        );

        for (int i = 1; i <= 80; i++) {
            String name = realNames.get(i-1);
            LockerDTO lockerDTO = new LockerDTO();
            lockerDTO.setLocker_num(i);
            lockerDTO.setName(name);
            lockerDTO.setClass_num(i+2);
            lockerDTO.setClass_code(1);
            lockerDTO.setRegion_code(2);
            lockerDTO.setGeneration_code(1);
            lockerDTO.setId(i+2);

            LockerEntity lockerEntity = LockerEntity.toLockerEntity(lockerDTO);
            lockerRepository.save(lockerEntity);
        }

        List<String> exampleNames = Arrays.asList(
                "차선호", "김희웅", "이영준", "김제준", "최영태", "최혜원",
                "김은하", "박성욱", "오희주", "이현섭", "장성운", "최희주",
                "김준성", "최재원", "박현우", "장진욱", "권민재", "김형진",
                "김도연", "김진영"
        );

        for (int i = 1; i <= 20; i++) {
            String name = realNames.get(i-1);
            SeatDTO seatDTO = new SeatDTO();
            seatDTO.setSeat_num(i);
            seatDTO.setName(name);
            seatDTO.setClass_num(i+2);
            seatDTO.setClass_code(1);
            seatDTO.setRegion_code(2);
            seatDTO.setGeneration_code(1);
            seatDTO.setId(i+2);

            SeatEntity seatEntity = SeatEntity.toSeatEntity(seatDTO);
            seatRepository.save(seatEntity);
        };

        // 중고거래 더미데이터
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1);
        postDTO.setRegister_time(1591563808494L);
        postDTO.setTitle("[공지] 중고거래 서비스 사용 안내");
        postDTO.setContent("1. 프로필 이미지를 클릭하면 대상의 정보를 볼 수 있습니다. \n\n2. 게시글 등록하면 수정이 불가합니다.\n\n3. 모두가 함께 사용하는 공간인 만큼 매너있게 사용합니다!! :D");
        postDTO.setProfile_image("/home/ubuntu/statics/images/profile_images/정수빈.png");
        postDTO.setName("정수빈");
        PostEntity postEntity = PostEntity.toPostEntity(postDTO);
        postRepository.save(postEntity);
    }
}
