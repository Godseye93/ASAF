package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.ProDTO;
import com.codingrecipe.member.service.ProService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor // proService 필드를 초기화하는 생성자를 자동으로 생성해 준다.
public class ProController {
    // 생성자 주입
    private final ProService proService;

    @PostMapping("/pro/save") // 회원가입 버튼 누를 시 ProController - ProService - ProEntity 순으로 참고해서 실행
    public ResponseEntity<String> save(@ModelAttribute ProDTO proDTO) { // 이메일, 비밀번호, 이름이 proDTO 에 담긴다.
        System.out.println("ProController.save");
        System.out.println("proDTO = " + proDTO);
        proService.save(proDTO);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }

    @PostMapping("/pro/login")
    public ResponseEntity<String> login(@ModelAttribute ProDTO proDTO, HttpSession session) {
        ProDTO loginResult = proService.login(proDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginEmail", loginResult.getProEmail());
            // ResponseEntity 상태코드 반환 추가 (HttpStatus.OK: 200)
            return ResponseEntity.status(HttpStatus.OK).body("로그인 성공!");
        } else {
            // login 실패
            // ResponseEntity 상태코드 반환 추가 (HttpStatus.UNAUTHORIZED: 401)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패!");
        }
    }


    @GetMapping("/pro/")
    public ResponseEntity<List<ProDTO>> findAll() {
        List<ProDTO> proDTOList = proService.findAll();

        return new ResponseEntity<>(proDTOList, HttpStatus.OK);
    }

    @GetMapping("/pro/{id}")
    public ResponseEntity<ProDTO> findById(@PathVariable Long id) {
        ProDTO proDTO = proService.findById(id);

        return new ResponseEntity<>(proDTO, HttpStatus.OK);
    }


    @PostMapping("/pro/update")
    public ResponseEntity<ProDTO> update(@ModelAttribute ProDTO proDTO) {
        proService.update(proDTO);
        return new ResponseEntity<>(proDTO, HttpStatus.OK);
    }

    @GetMapping("/pro/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        proService.deleteById(id);
        return new ResponseEntity<>("회원탈퇴 성공", HttpStatus.OK);
    }

    @GetMapping("/pro/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        System.out.println(session);
        return new ResponseEntity<>("로그아웃 성공",HttpStatus.OK);
    }

    @PostMapping("/pro/email-check")
    public @ResponseBody String emailCheck(@RequestParam("proEmail") String proEmail) {
        System.out.println("proEmail = " + proEmail);
        String checkResult = proService.emailCheck(proEmail);
        return checkResult;
    }
}
