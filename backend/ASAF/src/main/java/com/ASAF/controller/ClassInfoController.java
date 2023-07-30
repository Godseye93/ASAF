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

    // id 값으로 class_num, class_code, region_code, generation_code 조회
    @GetMapping("/{Id}")
    public ClassInfoDTO getClassInfoByMemberId(@PathVariable int Id) {
        return classInfoService.getClassInfoById(Id);
    }
}

