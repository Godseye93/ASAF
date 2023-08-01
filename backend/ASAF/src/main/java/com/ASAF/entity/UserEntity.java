package com.ASAF.entity;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ASAF.dto.MemberDTO;
import com.sun.istack.NotNull;
import lombok.Data;

// `@Data` - Lombok 라이브러리에서 제공하는 주석으로,
// 클래스 내의 'getter', 'setter', 'toString', 'equals', 'hashCode' 등의 메소드를 자동으로 생성합니다.
// `@Entity` - 클래스를 JPA 엔티티임을 선언하며, 이 클래스는 데이터베이스 테이블과 매핑됩니다.
@Data
@Entity
public class UserEntity {
    // 'UserEntity'와 'RoleEntity' 사이에 다대다 관계를 정의합니다.
    // 여기서 fetch타입을 EAGER로 설정하면, UserEntity를 로딩할 때 관련된 RoleEntity도 함께 로딩됩니다.
    @ManyToMany(fetch = FetchType.EAGER)
    // 사용자와 연관된 역할 집합을 나타냅니다.
    private Set<RoleEntity> roles;

    // mappedBy를 지정하여 양방향 관계를 매핑하고 @OneToOne 어노테이션에 cascade 옵션을 추가하여 연관 엔티티에서 변경이 있을 때 영향이 전파되도록 합니다.
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<ClassInfoEntity> classInfoEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "generation_code", cascade = CascadeType.ALL)
    private List<GenerationEntity> generationEntityList = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int electronic_student_id;

    @Column
    private String member_info;

    @Column
    private String birth_date;

    @Column(unique = true)
    private String memberEmail;

    @Column
    private String student_number;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    @Column
    private String phone_number;

    @Column
    private String profile_image;

    @Column
    private String authority;

    @Column
    private String token;

    @Column
    private String attended;

    @Column
    private Time entryTime;

    @Column
    private Time exitTime;

    //  MemberDTO 객체를 데이터베이스에 저장하거나 업데이트하기 위해 MemberEntity로 변환해줍니다.
    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setStudent_number(memberDTO.getStudent_number());
        memberEntity.setBirth_date(memberDTO.getBirth_date());
        memberEntity.setPhone_number(memberDTO.getPhone_number());
        memberEntity.setProfile_image(memberDTO.getProfile_image());
        memberEntity.setElectronic_student_id(memberDTO.getElectronic_student_id());
        memberEntity.setMember_info(memberDTO.getMember_info());
        memberEntity.setAuthority(memberDTO.getAuthority());
        memberEntity.setToken(memberDTO.getToken());
        memberEntity.setAttended(memberDTO.getAttended());
        memberEntity.setEntryTime(memberDTO.getEntryTime());
        memberEntity.setExitTime(memberDTO.getExitTime());

        return memberEntity;
    }

    public static MemberEntity toUpdateMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setStudent_number(memberDTO.getStudent_number());
        memberEntity.setBirth_date(memberDTO.getBirth_date());
        memberEntity.setPhone_number(memberDTO.getPhone_number());
        memberEntity.setProfile_image(memberDTO.getProfile_image());
        memberEntity.setElectronic_student_id(memberDTO.getElectronic_student_id());
        memberEntity.setMember_info(memberDTO.getMember_info());
        memberEntity.setAuthority(memberDTO.getAuthority());
        memberEntity.setToken(memberDTO.getToken());
        memberEntity.setAttended(memberDTO.getAttended());
        memberEntity.setEntryTime(memberDTO.getEntryTime());
        memberEntity.setExitTime(memberDTO.getExitTime());
        return memberEntity;
    }



}
