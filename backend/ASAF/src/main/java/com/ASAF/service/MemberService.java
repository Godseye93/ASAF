// 현재 파일이 com.codingrecipe.member.service 패키지에 포함되어 있음을 나타냅니다.
// 이 패키지는 주로 Service 계층의 클래스들을 포함하며, 비즈니스 로직을 처리합니다.
package com.ASAF.service;
// 이 구문은 다른 패키지에 있는 클래스를 현재 파일에서 사용할 수 있도록 가져옵니다
// MemberDTO 클래스를 가져옵니다.
// MemberEntity 클래스를 가져옵니다.
// MemberRepository 인터페이스를 가져옵니다.
// Lombok 및 Spring에 관련된 클래스와 어노테이션도 가져옵니다.
import com.ASAF.dto.MemberDTO;
import com.ASAF.repository.MemberRepository;
import com.ASAF.entity.MemberEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.data.crossstore.ChangeSetPersister;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// @Service 어노테이션:
//이 어노테이션은 클래스가 Spring Framework의 Service 계층
// (Component 어노테이션을 사용하는 경우도 있지만, 보통 Service 계층의 클래스에는 명시적으로 Service 어노테이션을 사용합니다)
// 에 속하며, 비즈니스 로직을 수행하는 데 사용됨을 나타냅니다. 이 클래스는 Controller와 Repository 사이에서 중간처리를 담당합니다.
// @RequiredArgsConstructor 어노테이션:
// 이 어노테이션은 Lombok 라이브러리의 일부입니다. 주로 클래스에 선언된 final 필드들 중 매개변수가 있는 생성자를 자동으로 생성합니다.
// 여기서는 MemberRepository를 주입(inject) 받기 위해 사용됩니다.
@Service
@Scope("prototype")
public class MemberService{
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 이 메서드는 MemberDTO 객체를 받아서 데이터베이스에 저장합니다.
    // 먼저 MemberEntity.toMemberEntity(memberDTO)를 호출해 MemberDTO 객체를 MemberEntity 객체로 변환한 후, memberRepository.save(memberEntity)를 호출하여 데이터베이스에 저장합니다.
    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    // 이 메서드는 로그인 처리를 담당하며, 이메일과 비밀번호를 사용합니다. 데이터베이스에서 이메일로 사용자를 조회한 다음, 조회 결과가 있는 경우 비밀번호가 일치하는지 확인합니다.
    // 비밀번호가 일치하면, MemberDTO.toMemberDTO(memberEntity)를 호출해 MemberEntity를 MemberDTO로 변경하고 반환합니다.
    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (byMemberEmail.isPresent()) {
            MemberEntity memberEntity = byMemberEmail.get();
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }



    // 이 메서드는 모든 MemberEntity 객체를 데이터베이스에서 조회한 후, 이를 MemberDTO 객체의 목록으로 변환하여 반환합니다.
    // memberRepository.findAll()을 호출한 다음, 반복문을 사용하여 각각의 MemberEntity 객체를 MemberDTO.toMemberDTO(memberEntity)를 사용해 MemberDTO 객체로 변환합니다.
    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity: memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    // 이 메서드는 주어진 ID를 사용하여 데이터베이스에서 MemberEntity 객체를 조회하고, 이를 MemberDTO 객체로 변환한 후 반환합니다.
    // optionalMemberEntity.get()을 사용하여 MemberEntity를 추출한 다음, MemberDTO.toMemberDTO(optionalMemberEntity.get())를 사용해 변환합니다.
    public MemberDTO findById(int id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public MemberDTO findByIdMember(int id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
            MemberDTO memberDTO = MemberDTO.toMemberDTO(optionalMemberEntity.get());
            if (!"프로".equals(memberDTO.getAuthority())) {
                return memberDTO;
            }
        }
        return null;
    }




    public MemberDTO findByMemberEmail(String memberEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    // 이 메서드는 주어진 이메일을 사용하여 데이터베이스에서 MemberEntity 객체를 조회하고, 이를 MemberDTO 객체로 변환한 후 반환합니다.
    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    // 이 메서드는 MemberDTO 객체를 받아 데이터베이스의 기존 항목을 업데이트합니다.
    // MemberEntity.toUpdateMemberEntity(memberDTO)를 사용해 MemberDTO로부터 MemberEntity 객체를 생성한 후에, memberRepository.save()를 호출하여 업데이트를 수행합니다.
    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    // 이 메서드는 주어진 ID를 사용하여 MemberEntity 객체를 데이터베이스에서 삭제합니다. memberRepository.deleteById(id)를 호출하여 삭제합니다.
    public void deleteById(int id) {
        memberRepository.deleteById(id);
    }

    // 이 메서드는 주어진 이메일이 데이터베이스에 존재하는지 확인합니다.
    // 이메일이 이미 존재하면 false를 반환하고, 그렇지 않으면 true를 반환합니다.
    public boolean emailCheck(String memberEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if (byMemberEmail.isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    public void saveProfileImage(String memberEmail, MultipartFile file) throws IOException, ChangeSetPersister.NotFoundException {
        MemberEntity memberEntity = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());

//        String UPLOAD_DIR = "src/main/resources/static/images/profile_images/";
        String UPLOAD_DIR = "/home/ubuntu/statics/images/profile_images/";
        String fileName = file.getOriginalFilename();
        String filePath = UPLOAD_DIR + memberEmail + "_" + fileName;
        File dest = new File(filePath);
        FileCopyUtils.copy(file.getBytes(), dest);

        memberEntity.setProfile_image(filePath);
        memberRepository.save(memberEntity);
    }
    
    // 이메일로 프로필 사진 주소 가져오는 메소드
    public String getProfileImagePath(String memberEmail) throws ChangeSetPersister.NotFoundException {
        MemberEntity memberEntity = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with email: " + memberEmail));
        return memberEntity.getProfile_image();
    }

    public MemberDTO CheckIn(int id) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findById(id);
        LocalDateTime currentTime = LocalDateTime.now();

        // 현재 시간을 long 타입으로 변경
        long currentTimeMillis = currentTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        if (memberEntityOptional.isPresent()){
            MemberEntity memberEntity = memberEntityOptional.get();

            // Time 타입 대신 long 타입으로 변경
            memberEntity.setEntryTime(currentTimeMillis);

            memberEntity.setAttended("입실");
            MemberEntity updatedMemberEntity = memberRepository.save(memberEntity);
            return MemberDTO.toMemberDTO(updatedMemberEntity);
        }
        return null;
    }


    public MemberDTO CheckOut(int id) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findById(id);
        LocalDateTime currentTime = LocalDateTime.now();

        // 현재 시간을 long 타입으로 변경..
        long currentTimeMillis = currentTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (memberEntityOptional.isPresent()){
            MemberEntity memberEntity = memberEntityOptional.get();
            memberEntity.setEntryTime(currentTimeMillis);
            memberEntity.setAttended("퇴실");
            MemberEntity updatedMemberEntity = memberRepository.save(memberEntity);
            return MemberDTO.toMemberDTO(updatedMemberEntity);
        }
        return null;
    }

}
