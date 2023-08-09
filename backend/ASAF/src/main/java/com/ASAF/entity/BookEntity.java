package com.ASAF.entity;

import com.ASAF.dto.BookDTO;
import com.ASAF.dto.RegionDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
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

//    @Temporal(TemporalType.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Long borrowDate;

//    @Temporal(TemporalType.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Long returnDate;

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

    public static BookEntity toBookEntity(BookDTO bookDTO) {
        BookEntity bookEntity = new BookEntity();

        bookEntity.setBookName(bookDTO.getBookName());
        bookEntity.setAuthor(bookDTO.getAuthor());
        bookEntity.setPublisher(bookDTO.getPublisher());
        bookEntity.setBorrowState(bookDTO.getBorrowState());
        bookEntity.setBorrowDate(bookDTO.getBorrowDate());
        bookEntity.setReturnDate(bookDTO.getReturnDate());

        ClassEntity classEntity = new ClassEntity();
        classEntity.setClass_code(bookDTO.getClass_code());
        bookEntity.setClass_code(classEntity);

        ClassInfoEntity classInfoEntity = new ClassInfoEntity();
        classInfoEntity.setClass_num(bookDTO.getClass_num());
        bookEntity.setClass_num(classInfoEntity);

        RegionEntity regionEntity = new RegionEntity();
        regionEntity.setRegion_code(bookDTO.getRegion_code());
        bookEntity.setRegion_code(regionEntity);

        GenerationEntity generationEntity = new GenerationEntity();
        generationEntity.setGeneration_code(bookDTO.getGeneration_code());
        bookEntity.setGeneration_code(generationEntity);

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(bookDTO.getId());
        bookEntity.setId(memberEntity);
        return bookEntity;
    }
}
