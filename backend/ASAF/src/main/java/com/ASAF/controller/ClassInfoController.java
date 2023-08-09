package com.ASAF.controller;

import com.ASAF.dto.ClassInfoDTO;
import com.ASAF.dto.MemberDTO;
import com.ASAF.entity.ClassEntity;
import com.ASAF.entity.GenerationEntity;
import com.ASAF.entity.RegionEntity;
import com.ASAF.service.ClassInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/create")
    public ResponseEntity<Boolean> createClassInfo(@RequestParam int Id,
                                        @RequestParam int class_code,
                                        @RequestParam int region_code,
                                        @RequestParam int generation_code) {
        // ClassInfoDTO 생성
        ClassInfoDTO classInfoDTO = new ClassInfoDTO();
        classInfoDTO.setId(Id);
        classInfoDTO.setClass_code(class_code);
        classInfoDTO.setRegion_code(region_code);
        classInfoDTO.setGeneration_code(generation_code);

        // DTO를 저장하고 반환된 객체를 반환
        return new ResponseEntity<>(true, HttpStatus.OK);
    }


}