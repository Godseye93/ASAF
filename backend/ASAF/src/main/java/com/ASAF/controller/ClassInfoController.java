package com.ASAF.controller;

import com.ASAF.dto.ClassInfoDTO;
import com.ASAF.dto.MemberDTO;
import com.ASAF.service.ClassInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    public ResponseEntity<String> createClassInfo(@RequestParam int Id,
                                                  @RequestParam int class_code,
                                                  @RequestParam int region_code,
                                                  @RequestParam int generation_code) {
        try {
            // ClassInfoDTO 생성
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            classInfoDTO.setId(Id);
            classInfoDTO.setClass_code(class_code);
            classInfoDTO.setRegion_code(region_code);
            classInfoDTO.setGeneration_code(generation_code);

            // DTO를 저장하고 반환된 객체를 확인
            ClassInfoDTO savedClassInfo = classInfoService.saveClassInfo(classInfoDTO);

            // 저장이 성공적으로 이루어졌다면 'true'와 함께 상태 코드 200(OK) 반환
            return new ResponseEntity<>("true", HttpStatus.OK);
        } catch (Exception e) {
            // 예외가 발생하면 'false'와 함께 상태 코드 500(Internal Server Error) 반환
            return new ResponseEntity<>("false", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}