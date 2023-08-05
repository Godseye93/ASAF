// 현재 파일이 com.codingrecipe.member.repository 패키지에 속함을 나타냅니다.
package com.ASAF.repository;
// Spring Data JPA의 JpaRepository 인터페이스를 가져옵니다. 이 인터페이스를 상속함으로써 기본적인 CRUD 연산을 제공받을 수 있습니다.
import com.ASAF.entity.BusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
// java.util 패키지의 Optional 클래스를 가져옵니다. 이 클래스는 값을 포함하거나 포함하지 않을 수 있는 컨테이너 객체를 제공하며,
// 값이 null일 수 있는 상황에서 Null Pointer Exception을 방지하는 데 도움이 됩니다.


// MemberRepository 인터페이스는 Spring Data JPA의 JpaRepository 인터페이스를 상속하여 구현하고 있습니다.
// MemberEntity: 데이터베이스에서 다루는 엔티티 클래스를 나타냅니다. 이 클래스에 명시된 필드는 엔티티와 관련된 데이터를 표현하며, 자동으로 데이터베이스의 테이블과 매핑됩니다.
// Long: MemberEntity의 ID 필드 타입입니다. 이 ID는 엔티티를 고유하게 식별하는 값으로 사용됩니다.
public interface BusRepository extends JpaRepository<BusEntity, Integer> {
    Optional<BusEntity> findByBusNum(int busNum);

}
