// 현재 파일이 com.codingrecipe.member.dto 패키지에 포함되어 있음을 나타냅니다.
// 이 패키지는 주로 데이터 전송 객체(Data Transfer Object, DTO)를 포함하며, 계층 간 데이터 전달을 처리합니다.
package com.codingrecipe.member.dto;
// ProEntity 클래스를 현재 파일에서 사용하기 위해 com.codingrecipe.member.entity 패키지에서 가져옵니다.
// 이 클래스는 데이터베이스의 한 테이블과 매핑되는 JPA 엔티티 클래스입니다.
import com.codingrecipe.member.entity.ProEntity;
import lombok.*;

// @Getter: 각 필드에 대한 getter 메서드를 자동 생성합니다.
// @Setter: 각 필드에 대한 setter 메서드를 자동 생성합니다.
// @NoArgsConstructor: 매개변수가 없는 기본 생성자를 자동 생성합니다.
// @AllArgsConstructor: 모든 필드를 매개변수로 사용하는 생성자를 자동 생성합니다.
// @ToString: 클래스의 문자열 표현을 반환하는 toString 메서드를 자동 생성합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProDTO {
    private Long id;
    private String name;
    private Long pro_number;
    private String proEmail;
    private String password;
    private String phone_number;
    private String profile_image;

    // ProEntity 타입의 하나의 매개변수 proEntity를 입력으로 받고 ProDTO 객체를 반환합니다.
    // 이 메서드의 목적은 ProEntity 객체의 정보를 가져와 ProDTO 객체로 변환하는 것입니다.
    public static ProDTO toProDTO(ProEntity proEntity) {
        ProDTO proDTO = new ProDTO();
        proDTO.setId(proEntity.getId());
        proDTO.setName(proEntity.getName());
        proDTO.setPro_number(proEntity.getPro_number());
        proDTO.setProEmail(proEntity.getProEmail());
        proDTO.setPhone_number(proEntity.getPhone_number());
        proDTO.setProfile_image(proEntity.getProfile_image());
        proDTO.setPassword(proEntity.getPassword());
        return proDTO;
    }
}
