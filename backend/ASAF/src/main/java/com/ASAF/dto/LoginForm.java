// 사용자가 로그인 시 제공하는 사용자 이름과 비밀번호를 저장할 수 있는 DTO(Data Transfer Object)입니다.
package com.ASAF.dto;

import com.ASAF.entity.MemberEntity;
import lombok.*;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginForm {

    private int id;
    private int electronic_student_id;
    private String member_info;
    private String birth_date;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String student_number;
    private String phone_number;
    private String profile_image;
    private String authority;
    private String token;
    private String attended;
    private Time entryTime;
    private Time exitTime;

    // MemberEntity 타입의 하나의 매개변수 memberEntity를 입력으로 받고 MemberDTO 객체를 반환합니다.
    // 이 메서드의 목적은 MemberEntity 객체의 정보를 가져와 MemberDTO 객체로 변환하는 것입니다.
    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setStudent_number(memberEntity.getStudent_number());
        memberDTO.setBirth_date(memberEntity.getBirth_date());
        memberDTO.setPhone_number(memberEntity.getPhone_number());
        memberDTO.setProfile_image(memberEntity.getProfile_image());
        memberDTO.setElectronic_student_id(memberEntity.getElectronic_student_id());
        memberDTO.setMember_info(memberEntity.getMember_info());
        memberDTO.setAuthority(memberEntity.getAuthority());
        memberDTO.setToken(memberEntity.getToken());
        memberDTO.setAttended(memberEntity.getAttended());
        memberDTO.setEntryTime(memberEntity.getEntryTime());
        memberDTO.setExitTime(memberEntity.getExitTime());
        return memberDTO;
    }
}
