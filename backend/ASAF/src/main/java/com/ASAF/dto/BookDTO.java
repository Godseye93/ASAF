package com.ASAF.dto;

import com.ASAF.entity.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookDTO {
    private int book_number;
    private String bookName;
    private String author;
    private String publisher;
    private Long borrowDate;
    private Long returnDate;
    private boolean borrowState;
    private String borrower;

    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private int id;


    public BookEntity toEntity(ClassInfoEntity classInfo, ClassEntity classCode, RegionEntity regionCode, GenerationEntity generationCode, MemberEntity member) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setBook_number(this.book_number);
        bookEntity.setBookName(this.bookName);
        bookEntity.setAuthor(this.author);
        bookEntity.setPublisher(this.publisher);
        bookEntity.setBorrowState(this.borrowState);
        bookEntity.setBorrowDate(this.borrowDate);
        bookEntity.setReturnDate(this.returnDate);
        bookEntity.setBorrower(this.borrower);
        // Update the setter names
        bookEntity.setClass_num(classInfo);
        bookEntity.setClass_code(classCode);
        bookEntity.setRegion_code(regionCode);
        bookEntity.setGeneration_code(generationCode);
        bookEntity.setId(member);

        return bookEntity;
    }

    public BookDTO(BookEntity bookEntity) {
        this.book_number = bookEntity.getBook_number();
        this.bookName = bookEntity.getBookName();
        this.author = bookEntity.getAuthor();
        this.publisher = bookEntity.getPublisher();
        this.borrowDate = bookEntity.getBorrowDate();
        this.returnDate = bookEntity.getReturnDate();
        this.borrowState = bookEntity.isBorrowState();
        this.borrower = bookEntity.getBorrower();

        //외래키
        this.class_num = bookEntity.getClass_num().getClass_num();
        this.class_code = bookEntity.getClass_code().getClass_code();
        this.region_code = bookEntity.getRegion_code().getRegion_code();
        this.generation_code = bookEntity.getGeneration_code().getGeneration_code();
        this.id = bookEntity.getId().getId();
    }

    public void updateEntity(BookEntity bookEntity, ClassInfoEntity classInfo, ClassEntity classCode,
                             RegionEntity regionCode, GenerationEntity generationCode, MemberEntity member) {
        bookEntity.setBookName(this.bookName);
        bookEntity.setAuthor(this.author);
        bookEntity.setPublisher(this.publisher);
        bookEntity.setBorrowDate(this.borrowDate);
        bookEntity.setReturnDate(this.returnDate);
        bookEntity.setBorrowState(this.borrowState);
        bookEntity.setBorrower(this.borrower);

        bookEntity.setClass_num(classInfo);
        bookEntity.setClass_code(classCode);
        bookEntity.setRegion_code(regionCode);
        bookEntity.setGeneration_code(generationCode);
        bookEntity.setId(member);
    }

    public static BookDTO fromBookEntity(BookEntity bookEntity) {
        return new BookDTO(
                bookEntity.getBook_number(),
                bookEntity.getBookName(),
                bookEntity.getAuthor(),
                bookEntity.getPublisher(),
                bookEntity.getReturnDate(),
                bookEntity.getBorrowDate(),
                bookEntity.isBorrowState(),
                bookEntity.getBorrower(),
                bookEntity.getClassInfoEntity().getClass_num(),
                bookEntity.getClassEntity().getClass_code(),
                bookEntity.getRegionEntity().getRegion_code(),
                bookEntity.getGenerationEntity().getGeneration_code(),
                bookEntity.getMemberEntity().getId()
        );
    }

    public Long getFormattedBorrowDate() {
        return borrowDate;
    }

    public Long getFormattedReturnDate() {
        return returnDate;
    }

    public boolean getBorrowState() {
        return borrowState;
    }
    public void updateMemberId(BookEntity bookEntity, MemberEntity member) {
        bookEntity.setId(member);
    }
}
