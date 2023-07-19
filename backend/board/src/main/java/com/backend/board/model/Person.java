package com.backend.board.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name= "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
}