package com.ASAF.dto;

import com.ASAF.entity.ClassInfoEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassInfoDTO {
    private int class_num;
    private MemberDTO memberDTO;

    // ClassInfoEntity 타입의 하나의 매개변수 classInfoEntity를 입력으로 받고 ClassInfoDTO 객체를 반환합니다.
    // 이 메서드의 목적은 ClassInfoEntity 객체의 정보를 가져와 ClassInfoDTO 객체로 변환하는 것입니다.
    public static ClassInfoDTO toClassInfoDTO(ClassInfoEntity classInfoEntity) {
        ClassInfoDTO classInfoDTO = new ClassInfoDTO();
        classInfoDTO.setMemberDTO(MemberDTO.toMemberDTO(classInfoEntity.getUser_id()));
        return classInfoDTO;
    }
}