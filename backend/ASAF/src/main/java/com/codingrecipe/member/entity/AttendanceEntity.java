package com.codingrecipe.member.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attendance_table")
public class AttendanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @OneToOne
    @JoinColumn(name = "student_number", referencedColumnName = "student_number")
    private MemberEntity member;

    @Column(nullable = false)
    private Boolean attended;

    @Column
    private String entryTime;

    @Column
    private String exitTime;
}
