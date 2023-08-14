// 이것은 패키지 선언이며, 현재 클래스가 com.codingrecipe.member.controller 패키지에 속함을 나타냅니다.
// 패키지는 관련된 클래스들을 그룹화하고 캡슐화하는 데 도움이 됩니다.
package com.ASAF.controller;
// 프로젝트에서 정의한 MemberDTO 클래스를 사용하기 위함입니다.
import com.ASAF.dto.MemberDTO;
// 프로젝트에서 정의한 MemberService 클래스를 사용하기 위함입니다.
import com.ASAF.service.MemberService;
// Lombok 라이브러리를 사용하여 생성자를 자동화하기 위함입니다
import lombok.RequiredArgsConstructor;
// Spring 프레임워크에서 사용되는 클래스들입니다.
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
// HttpSession 클래스를 사용하기 위함입니다. 세션 관리와 관련된 기능을 제공합니다.
import javax.servlet.http.HttpSession;
// Java의 기본 List 인터페이스를 사용하기 위함입니다.
import java.nio.file.Paths;
import java.util.List;


// @Controller 어노테이션은 이 클래스가 Spring 프레임워크 컨트롤러 컴포넌트임을 나타냅니다.
// 이 어노테이션을 사용하면 Spring이 이 클래스를 관리하게 되고, 웹 요청을 처리하는 데 사용됩니다.
// @RequiredArgsConstructor 어노테이션은 Lombok 라이브러리의 기능 중 하나입니다.
// 이 어노테이션은 클래스에 final 필드나 @NonNull 필드에 대한 생성자를 자동으로 생성해줍니다.
// 이를 통해 생성자 작성을 생략할 수 있어 코드를 더 간결하게 만들 수 있습니다.
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    // MemberService 인스턴스를 final로 선언하며 생성자 주입을 이용해 주입받습니다.
    private final MemberService memberService;

    // 회원가입 정보를 저장하기 위한 요청을 처리합니다. 클라이언트가 회원가입 정보를 전송할 때, 이 메서드가 호출됩니다.
    // 전달받은 DTO 객체를 이용해 회원 정보를 저장하고, 회원가입 결과를 ResponseEntity 형태로 반환합니다.
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }

    // 사용자 로그인을 처리하는 요청을 처리합니다.
    // 전달받은 DTO 객체를 이용해 로그인 결과를 확인하고, 해당 결과에 따라 세션 정보를 설정하거나 반환합니다.
    @PostMapping("/login")
    public ResponseEntity<MemberDTO> login(@RequestBody MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return ResponseEntity.status(HttpStatus.OK).body(loginResult);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    // 모든 회원의 정보를 조회하는 요청을 처리합니다.
    // 모든 회원의 정보를 조회하고, 그 결과를 ResponseEntity 형태로 반환합니다.
    @GetMapping
    public ResponseEntity<List<MemberDTO>> findAll() {
        List<MemberDTO> memberDTOList = memberService.findAll();
        return new ResponseEntity<>(memberDTOList, HttpStatus.OK);
    }

    // 특정 회원의 정보를 조회하는 요청을 처리합니다.
    // 전달받은 id를 이용해 회원의 정보를 조회하고, 그 결과를 ResponseEntity 형태로 반환합니다.
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> findById(@PathVariable int id) {
        MemberDTO memberDTO = memberService.findById(id);
        System.out.println(memberDTO);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> findById_name(@PathVariable int id) {
        MemberDTO memberDTO = memberService.findById(id);
        System.out.println(memberDTO);
        return new ResponseEntity<>(memberDTO.getMemberName(), HttpStatus.OK);
    }

    @GetMapping("/email/{memberEmail}")
    public ResponseEntity<MemberDTO> findByMemberEmail(@PathVariable String memberEmail) {
        MemberDTO memberDTO = memberService.findByMemberEmail(memberEmail);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    // 회원 정보를 수정하는 요청을 처리합니다.
    // 전달받은 DTO 객체를 이용해 회원 정보를 수정하고, 수정된 결과를 ResponseEntity 형태로 반환합니다.
    @PutMapping("/update")
    public ResponseEntity<MemberDTO> update(@RequestBody MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    // 특정 회원의 정보를 삭제하는 요청을 처리합니다.
    // 전달받은 id를 이용해 회원의 정보를 삭제하고, 결과를 ResponseEntity 형태로 반환합니다.
    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) {
        memberService.deleteById(id);
        return new ResponseEntity<>("회원탈퇴 성공", HttpStatus.OK);
    }

    // 로그아웃 요청을 처리합니다.
    // 로그아웃 처리를 위해 세션을 무효화하고, 결과를 ResponseEntity 형태로 반환합니다.
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("로그아웃 성공",HttpStatus.OK);
    }

    @GetMapping("/email-check/{memberEmail}")
    public @ResponseBody boolean emailCheck(@PathVariable("memberEmail") String memberEmail) {
        boolean checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
    }


    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("memberEmail") String memberEmail,
                                         @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        try {
            memberService.saveProfileImage(memberEmail, file);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    // 프로필 이미지 가져오기
    @GetMapping("/{memberEmail}/profile-image")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String memberEmail) {
        try {
            String imagePath = memberService.getProfileImagePath(memberEmail);
            Resource image = new UrlResource(Paths.get(imagePath).toUri());
            System.out.println(Paths.get(imagePath).toUri());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename(image.getFilename())
                    .build());
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PostMapping("/checkin/{id}")
    public MemberDTO checkIn(@PathVariable int id) {
        return memberService.CheckIn(id);
    }

    @PostMapping("/checkout/{id}")
    public MemberDTO checkOut(@PathVariable int id) {
        return memberService.CheckOut(id);
    }

    @PutMapping("/tokenUpdate")
    public ResponseEntity<Boolean> tokenUpdate(@RequestParam int id, @RequestParam String token) {
        MemberDTO memberDTO = memberService.findById(id);
        memberDTO.setToken(token);
        memberService.update(memberDTO);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }
}


