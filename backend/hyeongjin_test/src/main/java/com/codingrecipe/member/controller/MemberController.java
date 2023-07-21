package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor // memberService 필드를 초기화하는 생성자를 자동으로 생성해 준다.
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;

    @PostMapping("/member/save") // 회원가입 버튼 누를 시 MemberController - MemberService - MemberEntity 순으로 참고해서 실행
    public ResponseEntity<String> save(@ModelAttribute MemberDTO memberDTO) { // 이메일, 비밀번호, 이름이 memberDTO 에 담긴다.
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return new ResponseEntity<>("회원가입 완료", HttpStatus.CREATED);
    }

    @PostMapping("/member/login")
    public ResponseEntity<String> login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            // ResponseEntity 상태코드 반환 추가 (HttpStatus.OK: 200)
            return ResponseEntity.status(HttpStatus.OK).body("로그인 성공");
        } else {
            // login 실패
            // ResponseEntity 상태코드 반환 추가 (HttpStatus.UNAUTHORIZED: 401)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패);
        }
    }


    @GetMapping("/member/")
    public ResponseEntity<List<MemberDTO>> findAll() {
        List<MemberDTO> memberDTOList = memberService.findAll();

        return new ResponseEntity<>(memberDTOList, HttpStatus.OK);
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<MemberDTO> findById(@PathVariable Long id) {
        MemberDTO memberDTO = memberService.findById(id);

        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }


    @PostMapping("/member/update")
    public ResponseEntity<String> update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return new ResponseEntity<>("수정 완료", HttpStatus.OK);
    }

    @GetMapping("/member/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println(session);
        System.out.println("로그아웃 성공");
        return "index";
    }

    @PostMapping("/member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
//        if (checkResult != null) {
//            return "ok";
//        } else {
//            return "no";
//        }
    }

}









