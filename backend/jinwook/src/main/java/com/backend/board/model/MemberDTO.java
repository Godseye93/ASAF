package com.backend.board.model;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class MemberDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    private String profileImage;

    private String birthday;

    private String phoneNumber;

    private String location;

    private Integer cohort;

    private String className;

    public String getMemberEmail() {
        return username; // username 대신 memberEmail로 수정해야 합니다.
    }
}
