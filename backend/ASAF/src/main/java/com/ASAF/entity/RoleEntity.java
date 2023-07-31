package com.ASAF.entity;

import javax.persistence.*;

@Entity
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    // 'name' 필드의 getter입니다. 'name' 값을 반환합니다.
    public String getName() {
        return name;
    }

    // 'name' 필드의 setter입니다. 'name' 값을 인수로 받아 업데이트합니다.
    public void setName(String name) {
        this.name = name;
    }
}
