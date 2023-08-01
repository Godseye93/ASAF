package com.ASAF.entity;

import com.ASAF.dto.BookDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "Book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int book_number;

    private String bookName;

    private String author;

    private String Publisher;

    private boolean borrowState;

    @Temporal(TemporalType.DATE)
    private Date borrowDate;

    @Temporal(TemporalType.DATE)
    private Date returnDate;

    private String borrower;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "id")
    private MemberEntity id;


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
