package com.ASAF.entity;

import com.ASAF.dto.DocumentDTO;
import com.ASAF.dto.SeatDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "seat_arrange")
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seat_id;

    @ManyToOne
    @JoinColumn(name = "doc_id")
    private DocumentEntity doc_id;

    @ManyToOne
    @JoinColumn(name = "class_num")
    private ClassInfoEntity class_num;

    @ManyToOne
    @JoinColumn(name = "class_code")
    private ClassEntity class_code;

    @ManyToOne
    @JoinColumn(name = "region_code")
    private RegionEntity region_code;

    @ManyToOne
    @JoinColumn(name = "generation_code")
    private GenerationEntity generation_code;

    @ManyToOne
    @JoinColumn(name = "id")
    private MemberEntity id;

    @Column
    private int seat_num;

    @Column
    private String name;

    public DocumentEntity getDocumentEntity() {
        return doc_id;
    }
    public ClassInfoEntity getClassInfoEntity() {
        return class_num;
    }
    public ClassEntity getClassEntity() {
        return class_code;
    }
    public RegionEntity getRegionEntity() {
        return region_code;
    }
    public GenerationEntity getGenerationEntity() {
        return generation_code;
    }
    public MemberEntity getMemberEntity() {
        return id;
    }
}
