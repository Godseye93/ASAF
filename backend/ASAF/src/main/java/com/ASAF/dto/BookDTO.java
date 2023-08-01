package com.ASAF.dto;

import com.ASAF.entity.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    private Date borrowDate;
    private Date returnDate;
    private boolean borrowState;
    private String borrower;


    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private int id;


    public BookDTO(BookEntity bookEntity) {
        this.book_number = bookEntity.getBook_number();
        this.bookName = bookEntity.getBook_name();
        this.author = bookEntity.getAuthor();
        this.publisher = bookEntity.getPublisher();
        this.borrowState = bookEntity.isBorrowState();
        this.borrowDate = bookEntity.getBorrowDate();
        this.returnDate = bookEntity.getReturnDate();
        this.class_code = bookEntity.getClassEntity().getClass_code();
        this.region_code = bookEntity.getRegionEntity().getRegion_code();
        this.generation_code = bookEntity.getGenerationEntity().getGeneration_code();
        this.borrower = bookEntity.getBorrower();
        this.id = bookEntity.getMemberEntity().getId();
    }
}
