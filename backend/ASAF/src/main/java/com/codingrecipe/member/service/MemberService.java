// 현재 파일이 com.codingrecipe.member.service 패키지에 포함되어 있음을 나타냅니다.
// 이 패키지는 주로 Service 계층의 클래스들을 포함하며, 비즈니스 로직을 처리합니다.
package com.codingrecipe.member.service;
// 이 구문은 다른 패키지에 있는 클래스를 현재 파일에서 사용할 수 있도록 가져옵니다
// MemberDTO 클래스를 가져옵니다.
// MemberEntity 클래스를 가져옵니다.
// MemberRepository 인터페이스를 가져옵니다.
// Lombok 및 Spring에 관련된 클래스와 어노테이션도 가져옵니다.
import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
@RequiredArgsConstructor
public class MemberService {
    // 클래스의 멤버 변수로서 MemberRepository의 인스턴스를 가리킵니다.
    // 이 변수는 클래스 생성자를 통해 주입됩니다 (이 경우 Lombok의 @RequiredArgsConstructor 어노테이션이 사용됩니다).
    private final MemberRepository memberRepository;

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
    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
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
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    // 이 메서드는 주어진 이메일이 데이터베이스에 존재하는지 확인합니다.
    // 이메일이 이미 존재하면 "중복된 이메일이 존재합니다."를 반환하고, 그렇지 않으면 "사용 가능한 이메일입니다."를 반환합니다.
    public String emailCheck(String memberEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if (byMemberEmail.isPresent()) {
            return "중복된 이메일이 존재합니다.";
        } else {
            return "사용가능 한 이메일입니다.";
        }
    }
}













