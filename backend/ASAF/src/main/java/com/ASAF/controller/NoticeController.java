package com.ASAF.controller;

import com.ASAF.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notice")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;


}
