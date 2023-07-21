//package com.codingrecipe.member.service;
//
//import com.codingrecipe.member.dto.MemberDTO;
//import com.codingrecipe.member.entity.MemberEntity;
//import com.codingrecipe.member.repository.proRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class ProService {
//    private final proRepository proRepository;
//    public void save(MemberDTO memberDTO) { // 이메일, 비밀번호, 이름 등의 정보를 받아온다.
//        // 1. dto -> entity 변환
//        // 2. repository의 save 메서드 호출
//        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
//        proRepository.save(memberEntity);
//        // repository의 save메서드 호출 (조건. entity객체를 넘겨줘야 함)
//    }
//
//    public MemberDTO login(MemberDTO memberDTO) {
//        /*
//            1. 회원이 입력한 이메일로 DB에서 조회를 함
//            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
//         */
//        Optional<MemberEntity> byMemberEmail = proRepository.findByMemberEmail(memberDTO.getMemberEmail());
//        if (byMemberEmail.isPresent()) {
//            // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
//            MemberEntity memberEntity = byMemberEmail.get();
//            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
//                // 비밀번호 일치
//                // entity -> dto 변환 후 리턴
//                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
//                return dto;
//            } else {
//                // 비밀번호 불일치(로그인실패)
//                return null;
//            }
//        } else {
//            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
//            return null;
//        }
//    }
//
//    public List<MemberDTO> findAll() {
//        List<MemberEntity> memberEntityList = proRepository.findAll();
//        List<MemberDTO> memberDTOList = new ArrayList<>();
//        for (MemberEntity memberEntity: memberEntityList) {
//            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
////            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
////            memberDTOList.add(memberDTO);
//        }
//        return memberDTOList;
//    }
//
//    public MemberDTO findById(Long id) {
//        Optional<MemberEntity> optionalMemberEntity = proRepository.findById(id);
//        if (optionalMemberEntity.isPresent()) {
////            MemberEntity memberEntity = optionalMemberEntity.get();
////            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
////            return memberDTO;
//            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
//        } else {
//            return null;
//        }
//
//    }
//
//    public MemberDTO updateForm(String myEmail) {
//        Optional<MemberEntity> optionalMemberEntity = proRepository.findByMemberEmail(myEmail);
//        if (optionalMemberEntity.isPresent()) {
//            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
//        } else {
//            return null;
//        }
//    }
//
//    public void update(MemberDTO memberDTO) {
//        proRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
//    }
//
//    public void deleteById(Long id) {
//        proRepository.deleteById(id);
//    }
//
//    public String emailCheck(String memberEmail) {
//        Optional<MemberEntity> byMemberEmail = proRepository.findByMemberEmail(memberEmail);
//        if (byMemberEmail.isPresent()) {
//            // 조회결과가 있다 -> 사용할 수 없다.
//            return "중복된 이메일이 존재합니다.";
//        } else {
//            // 조회결과가 없다 -> 사용할 수 있다.
//            return "사용가능 한 이메일입니다.";
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
