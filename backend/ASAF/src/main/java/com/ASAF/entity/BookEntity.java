package com.ASAF.entity;

import com.ASAF.dto.BookDTO;
import lombok.Data;

import javax.persistence.*;
import java.awt.print.Book;

@Data
@Entity
@Table(name = "Book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int book_number;

    private String Book_name;

    private String author;

    private String Publisher;

    @ManyToOne
    @JoinColumn(name = "class_num")
    private ClassInfoEntity classInfo;

    @ManyToOne
    @JoinColumn(name = "class_code")
    private ClassEntity classCode;

    @ManyToOne
    @JoinColumn(name = "region_code")
    private RegionEntity regionCode;

    @ManyToOne
    @JoinColumn(name = "generation_code")
    private GenerationEntity generationCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MemberEntity userId;


    public static BookEntity toBookEntity(BookDTO bookDTO){
        BookEntity bookEntity = new BookEntity();
//        bookEntity.setBook_number(bookDTO.getBook_number());
        bookEntity.setBook_name(bookDTO.getBook_name());
        bookEntity.setAuthor(bookDTO.getAuthor());
        bookEntity.setPublisher(bookDTO.getPublisher());
        return bookEntity;
    }

    public static BookEntity toUpdateBookEntity(BookDTO bookDTO) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setBook_number(bookDTO.getBook_number());
        bookEntity.setBook_name(bookDTO.getBook_name());
        bookEntity.setAuthor(bookDTO.getAuthor());
        bookEntity.setPublisher(bookDTO.getPublisher());
        return bookEntity;
    }
}
