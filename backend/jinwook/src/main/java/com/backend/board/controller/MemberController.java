package com.backend.board.controller;

import com.backend.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.backend.board.model.MemberDTO;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/register")
    public ResponseEntity<String> registerUser(@RequestBody MemberDTO memberDTO) {
        // 비밀번호를 해싱하여 저장
        String encodedPassword = passwordEncoder.encode(memberDTO.getPassword());
        memberDTO.setPassword(encodedPassword);

        // 회원가입 서비스 호출
        memberService.registerUser(memberDTO);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/api/login") //@ModelAttribute와 @RequestBody 차이
    public ResponseEntity<String> loginUser(@RequestBody MemberDTO memberDTO) { // HttpSession session
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // 로그인 성공
            return ResponseEntity.ok("로그인에 성공했습니다.");
        } else {
            // 로그인 실패
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패했습니다.");
        }
    }

    @GetMapping("/api/members")
    public ResponseEntity<List<MemberDTO>> findAllMembers() {
        List<MemberDTO> memberDTOList = memberService.findAllMembers();
        return ResponseEntity.ok(memberDTOList);
    }

    @GetMapping("/api/members/{id}")
    public ResponseEntity<MemberDTO> findMemberById(@PathVariable Long id) {
        MemberDTO memberDTO = memberService.findMemberById(id);
        if (memberDTO != null) {
            return ResponseEntity.ok(memberDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/members/update")
    public ResponseEntity<String> updateMember(@RequestBody MemberDTO memberDTO) {
        memberService.updateMember(memberDTO);
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }

    @DeleteMapping("/api/members/delete/{id}")
    public ResponseEntity<String> deleteMemberById(@PathVariable Long id) {
        memberService.deleteMemberById(id);
        return ResponseEntity.ok("회원 정보가 삭제되었습니다.");
    }

    @PostMapping("/api/email-check")
    public ResponseEntity<String> emailCheck(@RequestParam("memberEmail") String memberEmail) {
        String checkResult = memberService.emailCheck(memberEmail);
        return ResponseEntity.ok(checkResult);
    }


}
