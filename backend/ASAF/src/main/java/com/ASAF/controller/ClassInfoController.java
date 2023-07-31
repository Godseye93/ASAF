package com.ASAF.controller;

import com.ASAF.dto.ClassInfoDTO;
import com.ASAF.entity.ClassInfoEntity;
import com.ASAF.entity.MemberEntity;
import com.ASAF.service.ClassInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/classinfo")
@RestController
@RequiredArgsConstructor
public class ClassInfoController {
    private final ClassInfoService classInfoService;

    @GetMapping("/member/{memberId}")
    public List<ClassInfoDTO> getClassInfoListByMemberId(@PathVariable int memberId) {
        return classInfoService.getClassInfoByMemberId(memberId);
    }
}



