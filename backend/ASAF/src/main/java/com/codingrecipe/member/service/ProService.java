// 현재 파일이 com.codingrecipe.member.service 패키지에 포함되어 있음을 나타냅니다.
// 이 패키지는 주로 Service 계층의 클래스들을 포함하며, 비즈니스 로직을 처리합니다.
package com.codingrecipe.member.service;
// 이 구문은 다른 패키지에 있는 클래스를 현재 파일에서 사용할 수 있도록 가져옵니다
// ProDTO 클래스를 가져옵니다.
// ProEntity 클래스를 가져옵니다.
// ProRepository 인터페이스를 가져옵니다.
// Lombok 및 Spring에 관련된 클래스와 어노테이션도 가져옵니다.
import com.codingrecipe.member.dto.ProDTO;
import com.codingrecipe.member.entity.ProEntity;
import com.codingrecipe.member.repository.ProRepository;
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
// 여기서는 ProRepository를 주입(inject) 받기 위해 사용됩니다.
@Service
@RequiredArgsConstructor
public class ProService {
    // 클래스의 멤버 변수로서 ProRepository의 인스턴스를 가리킵니다.
    // 이 변수는 클래스 생성자를 통해 주입됩니다 (이 경우 Lombok의 @RequiredArgsConstructor 어노테이션이 사용됩니다).
    private final ProRepository proRepository;

    // 이 메서드는 ProDTO 객체를 받아서 데이터베이스에 저장합니다.
    // 먼저 ProEntity.toProEntity(proDTO)를 호출해 ProDTO 객체를 ProEntity 객체로 변환한 후, proRepository.save(proEntity)를 호출하여 데이터베이스에 저장합니다.
    public void save(ProDTO proDTO) {
        ProEntity proEntity = ProEntity.toProEntity(proDTO);
        proRepository.save(proEntity);
    }

    // 이 메서드는 로그인 처리를 담당하며, 이메일과 비밀번호를 사용합니다. 데이터베이스에서 이메일로 사용자를 조회한 다음, 조회 결과가 있는 경우 비밀번호가 일치하는지 확인합니다.
    // 비밀번호가 일치하면, ProDTO.toProDTO(proEntity)를 호출해 ProEntity를 ProDTO로 변경하고 반환합니다.
    public ProDTO login(ProDTO proDTO) {
        Optional<ProEntity> byProEmail = proRepository.findByProEmail(proDTO.getProEmail());
        if (byProEmail.isPresent()) {
            ProEntity proEntity = byProEmail.get();
            if (proEntity.getPassword().equals(proDTO.getPassword())) {
                ProDTO dto = ProDTO.toProDTO(proEntity);
                return dto;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // 이 메서드는 모든 ProEntity 객체를 데이터베이스에서 조회한 후, 이를 ProDTO 객체의 목록으로 변환하여 반환합니다.
    // proRepository.findAll()을 호출한 다음, 반복문을 사용하여 각각의 ProEntity 객체를 ProDTO.toProDTO(proEntity)를 사용해 ProDTO 객체로 변환합니다.
    public List<ProDTO> findAll() {
        List<ProEntity> proEntityList = proRepository.findAll();
        List<ProDTO> proDTOList = new ArrayList<>();
        for (ProEntity proEntity: proEntityList) {
            proDTOList.add(ProDTO.toProDTO(proEntity));
        }
        return proDTOList;
    }

    // 이 메서드는 주어진 ID를 사용하여 데이터베이스에서 ProEntity 객체를 조회하고, 이를 ProDTO 객체로 변환한 후 반환합니다.
    // optionalProEntity.get()을 사용하여 ProEntity를 추출한 다음, ProDTO.toProDTO(optionalProEntity.get())를 사용해 변환합니다.
    public ProDTO findById(Long id) {
        Optional<ProEntity> optionalProEntity = proRepository.findById(id);
        if (optionalProEntity.isPresent()) {
            return ProDTO.toProDTO(optionalProEntity.get());
        } else {
            return null;
        }

    }

    // 이 메서드는 주어진 이메일을 사용하여 데이터베이스에서 ProEntity 객체를 조회하고, 이를 ProDTO 객체로 변환한 후 반환합니다.
    public ProDTO updateForm(String myEmail) {
        Optional<ProEntity> optionalProEntity = proRepository.findByProEmail(myEmail);
        if (optionalProEntity.isPresent()) {
            return ProDTO.toProDTO(optionalProEntity.get());
        } else {
            return null;
        }
    }

    // 이 메서드는 ProDTO 객체를 받아 데이터베이스의 기존 항목을 업데이트합니다.
    // ProEntity.toUpdateProEntity(proDTO)를 사용해 ProDTO로부터 ProEntity 객체를 생성한 후에, proRepository.save()를 호출하여 업데이트를 수행합니다.
    public void update(ProDTO proDTO) {
        proRepository.save(ProEntity.toUpdateProEntity(proDTO));
    }

    // 이 메서드는 주어진 ID를 사용하여 ProEntity 객체를 데이터베이스에서 삭제합니다. proRepository.deleteById(id)를 호출하여 삭제합니다.
    public void deleteById(Long id) {
        proRepository.deleteById(id);
    }

    // 이 메서드는 주어진 이메일이 데이터베이스에 존재하는지 확인합니다.
    // 이메일이 이미 존재하면 "중복된 이메일이 존재합니다."를 반환하고, 그렇지 않으면 "사용 가능한 이메일입니다."를 반환합니다.
    public String emailCheck(String proEmail) {
        Optional<ProEntity> byProEmail = proRepository.findByProEmail(proEmail);
        if (byProEmail.isPresent()) {
            return "중복된 이메일이 존재합니다.";
        } else {
            return "사용가능 한 이메일입니다.";
        }
    }
}
