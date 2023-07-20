package com.codingrecipe.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 사용자가 요청한 URL을 처리하고 응답을 보내는 클래스를 사용할 때 사용하는 어노테이션이다.
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    } // resources/templates/index.html 로 이동
}
