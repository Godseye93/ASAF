package com.ASAF.controller;

import com.ASAF.dto.ClassInfoDTO;
import com.ASAF.dto.MemberDTO;
import com.ASAF.entity.ClassEntity;
import com.ASAF.entity.GenerationEntity;
import com.ASAF.entity.RegionEntity;
import com.ASAF.service.ClassInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RequestMapping("/classinfo")
@RestController
@RequiredArgsConstructor
public class ClassInfoController {
    private final ClassInfoService classInfoService;

    @GetMapping("/member/{memberId}")
    public List<ClassInfoDTO> getClassInfoListByMemberId(@PathVariable int memberId) {
        return classInfoService.getClassInfoByMemberId(memberId);
    }
    @GetMapping("/memberIds")
    public List<MemberDTO> findMemberDTOsByClassRegionAndGeneration(@RequestParam int class_code,
                                                                    @RequestParam int region_code,
                                                                    @RequestParam int generation_code) {
        return classInfoService.findMemberDTOsByClassRegionAndGeneration(class_code, region_code, generation_code);
    }
}