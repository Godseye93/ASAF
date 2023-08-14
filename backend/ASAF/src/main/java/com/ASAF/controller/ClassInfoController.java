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

    // 학생정보로 반정보 불러오기 /${id}
    @GetMapping("/member/{memberId}")
    public List<ClassInfoDTO> getClassInfoListByMemberId(@PathVariable int memberId) {
        return classInfoService.getClassInfoByMemberId(memberId);
    }

    //반정보로 학생정보목록 불러오기
    @GetMapping("/memberIds")
    public List<MemberDTO> findMemberDTOsByClassRegionAndGeneration(@RequestParam int class_code,
                                                                    @RequestParam int region_code,
                                                                    @RequestParam int generation_code) {
        return classInfoService.findMemberDTOsByClassRegionAndGeneration(class_code, region_code, generation_code);
    }

    // 반정보로 프로를 제외한 학생정보 목록 불러오기
    @GetMapping("/pro/memberIds")
    public List<MemberDTO> findMemberDTOsByClassRegionAndGenerationpro(@RequestParam int class_code,
                                                                    @RequestParam int region_code,
                                                                    @RequestParam int generation_code) {
        return classInfoService.findMemberDTOsByClassRegionAndGenerationpro(class_code, region_code, generation_code);
    }

    // 학생id와 반정보를 입력하여 classinfo에 데이터 넣기
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
    // 학생id로 classinfo 데이터 수정하기
    @PutMapping("/{class_num}")
    public ClassInfoDTO updateClassInfo(@PathVariable int class_num, @RequestBody ClassInfoDTO classInfoDTO) {
        return classInfoService.updateClassInfo(class_num, classInfoDTO);
    }

    //학생id로 classinfo 데이터 삭제하기
    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> removeClassInfo(@PathVariable int memberId) {
        try {
            classInfoService.removeClassInfoByMemberId(memberId);

            // 삭제가 성공적으로 이루어졌다면 'true'와 함께 상태 코드 200(OK) 반환
            return new ResponseEntity<>("true", HttpStatus.OK);
        } catch (Exception e) {
            // 예외가 발생하면 'false'와 함께 상태 코드 500(Internal Server Error) 반환
            return new ResponseEntity<>("false", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}