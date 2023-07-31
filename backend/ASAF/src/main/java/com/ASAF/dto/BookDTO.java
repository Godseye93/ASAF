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
    private String book_name;
    private String author;
    private String publisher;
    private boolean borrowState;
    private Date borrowDate;
    private Date returnDate;



    private ClassInfoDTO classInfoDTO;
    private ClassDTO classDTO;
    private RegionDTO regionDTO;
    private GenerationDTO generationDTO;


    public BookDTO(BookEntity bookEntity) {
        this.book_number = bookEntity.getBook_number();
        this.book_name = bookEntity.getBook_name();
        this.author = bookEntity.getAuthor();
        this.publisher = bookEntity.getPublisher();
        this.borrowState = bookEntity.isBorrowState();
        this.borrowDate = bookEntity.getBorrowDate();
        this.returnDate = bookEntity.getReturnDate();
        this.classInfoDTO = ClassInfoDTO.toClassInfoDTO(bookEntity.getClassInfoEntity());
        this.classDTO = ClassDTO.toClassDTO(bookEntity.getClassEntity());
        this.regionDTO = RegionDTO.toRegionDTO(bookEntity.getRegionEntity());
        this.generationDTO = GenerationDTO.toGenerationDTO(bookEntity.getGenerationEntity());
    }
}
