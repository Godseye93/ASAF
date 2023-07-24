package com.ASAF.dto;

import com.ASAF.entity.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookDTO {
    private int book_number;
    private String book_name;
    private String author;
    private String publisher;

    private int class_num;
    private int class_code;
    private int region_code;
    private int generation_code;
    private String user_id;


    public BookDTO(BookEntity bookEntity) {
        this.book_number = bookEntity.getBook_number();
        this.book_name = bookEntity.getBook_name();
        this.author = bookEntity.getAuthor();
        this.publisher = bookEntity.getPublisher();
    }
    public static BookDTO toBookDTO(BookEntity bookEntity) {
        BookDTO bookDto = new BookDTO();
        bookDto.setBook_number(bookEntity.getBook_number());
        bookDto.setBook_name(bookEntity.getBook_name());
        bookDto.setAuthor(bookEntity.getAuthor());
        bookDto.setPublisher(bookEntity.getPublisher());
        return bookDto;
    }
}
