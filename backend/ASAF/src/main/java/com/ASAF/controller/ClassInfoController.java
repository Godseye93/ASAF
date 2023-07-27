package com.ASAF.controller;

import com.ASAF.dto.ClassInfoDTO;
import com.ASAF.entity.MemberEntity;
import com.ASAF.service.ClassInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Member;


@RequestMapping("/classinfo")
@RestController
@RequiredArgsConstructor
public class ClassInfoController {
    private final ClassInfoService classInfoService;


    @GetMapping("/{id}")
    public ClassInfoDTO getClassInfoById(@PathVariable("id") int memberId) {
        return classInfoService.getClassInfoByMemberId(memberId);
    }
}

