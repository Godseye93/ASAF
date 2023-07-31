package com.ASAF.entity;

import javax.persistence.*;
import java.util.Set;
import lombok.Data;

// `@Data` - Lombok 라이브러리에서 제공하는 주석으로,
// 클래스 내의 'getter', 'setter', 'toString', 'equals', 'hashCode' 등의 메소드를 자동으로 생성합니다.
// `@Entity` - 클래스를 JPA 엔티티임을 선언하며, 이 클래스는 데이터베이스 테이블과 매핑됩니다.
@Data
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;

    // 'UserEntity'와 'RoleEntity' 사이에 다대다 관계를 정의합니다.
    // 여기서 fetch타입을 EAGER로 설정하면, UserEntity를 로딩할 때 관련된 RoleEntity도 함께 로딩됩니다.
    @ManyToMany(fetch = FetchType.EAGER)
    // 사용자와 연관된 역할 집합을 나타냅니다.
    private Set<RoleEntity> roles;

    // 'roles' 필드의 getter입니다. 'roles' 집합을 반환합니다.
    public Set<RoleEntity> getRoles() {
        return roles;
    }

    // 'roles' 필드의 setter입니다. 'roles' 집합을 인수로 받아 업데이트합니다.
    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    // 'username' 필드의 getter입니다. 'username' 값을 반환합니다.
    public String getUsername() {
        return username;
    }

    // 'password' 필드의 getter입니다. 'password' 값을 반환합니다.
    public String getPassword() {
        return password;
    }

}
