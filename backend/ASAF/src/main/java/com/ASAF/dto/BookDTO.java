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
    private ClassInfoDTO classInfoDTO;
    private ClassDTO classDTO;
    private RegionDTO regionDTO;
    private GenerationDTO generationDTO;


    public BookDTO(BookEntity bookEntity) {
        this.book_number = bookEntity.getBook_number();
        this.book_name = bookEntity.getBook_name();
        this.author = bookEntity.getAuthor();
        this.publisher = bookEntity.getPublisher();
        this.classInfoDTO = ClassInfoDTO.toClassInfoDTO(bookEntity.getClassInfoEntity());
        this.classDTO = ClassDTO.toClassDTO(bookEntity.getClassEntity());
        this.regionDTO = RegionDTO.toRegionDTO(bookEntity.getRegionEntity());
        this.generationDTO = GenerationDTO.toGenerationDTO(bookEntity.getGenerationEntity());
    }
    public static BookDTO toBookDTO(BookEntity bookEntity) {
        BookDTO bookDto = new BookDTO();
        bookDto.setBook_number(bookEntity.getBook_number());
        bookDto.setBook_name(bookEntity.getBook_name());
        bookDto.setAuthor(bookEntity.getAuthor());
        bookDto.setPublisher(bookEntity.getPublisher());
        bookDto.setClassInfoDTO(ClassInfoDTO.toClassInfoDTO(bookEntity.getClassInfoEntity()));
        bookDto.setClassDTO(ClassDTO.toClassDTO(bookEntity.getClassEntity()));
        bookDto.setRegionDTO(RegionDTO.toRegionDTO(bookEntity.getRegionEntity()));
        bookDto.setGenerationDTO(GenerationDTO.toGenerationDTO(bookEntity.getGenerationEntity()));
        return bookDto;
    }
}
